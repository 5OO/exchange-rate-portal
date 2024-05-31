package org.sberp.exchangerateportal.configuration;

import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@RequiredArgsConstructor
@Configuration
public class QuartzConfiguration {

    private final QuartzJobFactory jobFactory;
    private final Properties quartzProperties;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Trigger trigger, JobDetail jobDetail) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);
        factory.setJobDetails(jobDetail);
        factory.setTriggers(trigger);
        factory.setQuartzProperties(quartzProperties);
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(FetchExchangeRatesJob.class)
                .withIdentity("fetchExchangeRatesJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger() throws IOException {
        String cronSchedule = quartzProperties().getProperty("exchange.rate.job.cron.schedule");
        return TriggerBuilder.newTrigger()
                .withIdentity("fetchExchangeRatesTrigger")
                .forJob(jobDetail())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule))
                .build();
    }
}
