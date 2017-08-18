package com.tomtop.timetask.component;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tomtop.timetask.service.ITaskService;

@Component
public class SchedulerComponent {
	
	@Autowired
	private ITaskService taskService;

	public void excuteJob() throws SchedulerException {
		taskService.init();
	}

}
