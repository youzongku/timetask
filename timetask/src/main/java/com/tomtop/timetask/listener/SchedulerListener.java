package com.tomtop.timetask.listener;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.tomtop.timetask.component.SchedulerComponent;



@Configuration
public class SchedulerListener implements ApplicationListener<ContextRefreshedEvent>{
	
	@Autowired
	private SchedulerComponent schedulerComponent;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			schedulerComponent.excuteJob();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	@Bean
    public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext  applicationContext){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setApplicationContext(applicationContext);
        return schedulerFactoryBean;
    }
	

}
