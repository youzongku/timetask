package com.tomtop.timetask.excute;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.tomtop.timetask.utils.HttpUtil;

import play.libs.Json;

public class JobExcute implements Job {

	private static Logger logger = Logger.getLogger(JobExcute.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		String response = HttpUtil.get(jobDetail.getJobDataMap().getString("url"));
		System.err.println("执行中：" + Json.toJson(context.getJobDetail().getJobDataMap()));
		logger.info(response);
	}

}
