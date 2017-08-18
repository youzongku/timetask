package com.tomtop.timetask.service;

import com.tomtop.timetask.entity.TaskInfo;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.util.Map;

public interface ITaskService {

	Map<String, Object> create(String string);

	void registerTask(Scheduler scheduler, TaskInfo taskInfo) throws SchedulerException;

	void init();

	/**
	 * 暂停任务
	 * @author youz
	 * @date 2017/8/16
	 * @param jsonStr
	 * @return
	 */
	Map<String, Object> pause(String jsonStr);

	/**
	 * 删除任务
	 * @author youz
	 * @date 2017/8/16
	 * @param jsonStr
	 * @return
	 */
	Map<String, Object> deleteTask(String jsonStr);

	/**
	 * 暂停所有任务
	 * @author youz
	 * @date 2017/8/16
	 * @return
	 */
	Map<String, Object> pauseAllTask();

	/**
	 * 启动所有任务
	 * @author youz
	 * @date 2017/8/16
	 * @return
	 */
	Map<String, Object> startAllTask();

	/**
	 * 更新任务
	 * @author youz
	 * @date 2017/8/18
	 * @param jsonStr
	 * @return
	 */
	Map<String, Object> updateTask(String jsonStr);

}
