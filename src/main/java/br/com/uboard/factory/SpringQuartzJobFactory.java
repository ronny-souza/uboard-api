package br.com.uboard.factory;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component
public class SpringQuartzJobFactory extends SpringBeanJobFactory {

    private final ApplicationContext applicationContext;

    public SpringQuartzJobFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    @NonNull
    protected Object createJobInstance(@NonNull TriggerFiredBundle bundle) throws Exception {
        Object job = super.createJobInstance(bundle);
        this.applicationContext.getAutowireCapableBeanFactory().autowireBean(job);
        return job;
    }
}
