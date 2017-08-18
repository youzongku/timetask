package com.tomtop.timetask.listener;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.Json;

public class SchedulerJobListener implements JobListener {
	
	private static final Logger logger=LoggerFactory.getLogger(SchedulerJobListener.class);
	
	private String name;
	
	public SchedulerJobListener(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		try {
			Scheduler scheduler = context.getScheduler();
			JobDetail jobDetail = context.getJobDetail();
			System.err.println(scheduler.getJobGroupNames().toString());
			JobKey jobKey = context.getJobDetail().getKey();
			try {
				logger.info("当前执行中的任务{},状态为:{}",Json.toJson(jobDetail.getJobDataMap().get("name")),scheduler.getTriggerState(context.getTrigger().getKey()));
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		/*	logger.info("jobKey:{}",Json.toJson(jobKey));
			scheduler.pauseJob(context.getJobDetail().getKey());
			if(!scheduler.checkExists(jobKey)){
				scheduler.triggerJob(jobKey);
			}*/
//			
//		jobDetail = JobBuilder.newJob(JobExcute.class)
//				.withIdentity(taskInfo.getGroupName(), taskInfo.getGroupName())
//				.usingJobData("url", taskInfo.getUrl())
//				.usingJobData("cron", taskInfo.getCron())
//				.build();
//		scheduler.scheduleJob(jobDetail, triggersForJob, true);
		System.out.println("执行前：" + Json.toJson(context.getJobDetail().getJobDataMap()));
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		System.out.println("被否决执行了，可以做些日志记录。");

	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		System.out.println("执行后：" + Json.toJson(context.getJobDetail().getJobDataMap()));

	}

}
