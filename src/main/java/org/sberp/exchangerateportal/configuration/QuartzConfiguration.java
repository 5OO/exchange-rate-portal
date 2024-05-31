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

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Trigger trigger, JobDetail jobDetail) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);
        factory.setJobDetails(jobDetail);
        factory.setTriggers(trigger);
        factory.setQuartzProperties(quartzProperties());
        factory.setSchedulerName("MyUniqueQuartzScheduler");
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
    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity("fetchExchangeRatesTrigger")
                .forJob(jobDetail())
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(17, 29))
                .build();
    }
}
