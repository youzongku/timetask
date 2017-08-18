package com.tomtop.timetask.excute;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import play.libs.Json;

@Configuration
@EnableScheduling
public class RefreshJob {

	private Logger logger = LoggerFactory.getLogger(RefreshJob.class);

	@Autowired
	private SchedulerFactoryBean sfactoryBean;
	
	/**
	 * 	使用Quartz定时调度Job，经常需要实时监控Job的执行状态。在这里，Quartz提供了getTriggerState方法来获取当前执行状态。 
	 *	其中返回值分别代表意思如下： 
	 *	STATE_BLOCKED 4 阻塞 
	 *	STATE_COMPLETE 2 完成 
	 *	STATE_ERROR 3 错误 
	 *	STATE_NONE -1 不存在 
	 *	STATE_NORMAL 0 正常 
	 *	STATE_PAUSED 1 暂停** 
	*/
//	@Scheduled(fixedRate = 5000)
	public void checkJob() {
		try {
			Scheduler scheduler = sfactoryBean.getScheduler();
			scheduler.getCurrentlyExecutingJobs().forEach(job->{
				try {
					logger.info("当前执行中的任务{},状态为:{}",Json.toJson(job.getJobDetail().getJobDataMap().get("name")),scheduler.getTriggerState(job.getTrigger().getKey()));
				} catch (SchedulerException e) {
					e.printStackTrace();
				}
			});;
			logger.info("--" + scheduler.getCurrentlyExecutingJobs().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
