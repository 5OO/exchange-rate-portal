package org.sberp.exchangerateportal.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class QuartzJobFactory extends SpringBeanJobFactory {

    private final AutowireCapableBeanFactory beanFactory;

    @NotNull
    @Override
    public Object createJobInstance(@NotNull TriggerFiredBundle bundle) throws Exception {
        Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }
}
