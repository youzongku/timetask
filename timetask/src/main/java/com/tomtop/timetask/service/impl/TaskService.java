package com.tomtop.timetask.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.tomtop.timetask.entity.TaskInfo;
import com.tomtop.timetask.excute.JobExcute;
import com.tomtop.timetask.listener.SchedulerJobListener;
import com.tomtop.timetask.mapper.TaskInfoMapper;
import com.tomtop.timetask.service.ITaskService;
import com.tomtop.timetask.utils.CronUtil;
import com.tomtop.timetask.utils.JsonCaseUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import play.libs.Json;

import java.util.Date;
import java.util.Map;


/**
 * TODO
 * 新增任务
 * 修改任务状态
 * 暂停任务
 * 删除任务
 * 全部暂停
 * 全部触发
 *
 * @author Administrator
 */
@Service("taskService")
public class TaskService implements ITaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskInfoMapper taskMapper;

    @Autowired
    private SchedulerFactoryBean sfactoryBean;

    private static SchedulerJobListener listener = new SchedulerJobListener("JobListener");

    @Override
    public Map<String, Object> create(String jsonStr) {
        Map<String, Object> result = Maps.newHashMap();
        try {
            TaskInfo task = JsonCaseUtil.jsonToBean(jsonStr, TaskInfo.class);
            if (null == task) {
                result.put("suc", false);
                result.put("msg", "参数错误");
                return result;
            }
            if (!CronUtil.cronTest(task.getCron())) {
                result.put("suc", false);
                result.put("msg", "cron表达式有误");
                return result;
            }
            task.setCreateTime(new Date());
            taskMapper.insertSelective(task);
            newTask(task, sfactoryBean.getScheduler());
            result.put("suc", true);
            result.put("msg", "插入成功");
        } catch (Exception e) {
            logger.info("插入异常{}", e);
            result.put("suc", false);
            result.put("msg", "插入异常");
        }
        return result;
    }

    private void newTask(TaskInfo task, Scheduler scheduler) throws SchedulerException {
        registerTask(scheduler, task);
        scheduler.getListenerManager().addJobListener(listener);
    }

    @Override
    public void registerTask(Scheduler scheduler, TaskInfo taskInfo) throws SchedulerException {
        JobDetail jobDetail;
        CronScheduleBuilder scheduleBuilder;
        CronTrigger cronTrigger;
        jobDetail = JobBuilder.newJob(JobExcute.class)
                .withIdentity(taskInfo.getTaskName(), taskInfo.getGroupName())
                .usingJobData("url", taskInfo.getUrl())
                .usingJobData("cron", taskInfo.getCron())
                .usingJobData("name", taskInfo.getTaskName())
                .build();
        scheduleBuilder = CronScheduleBuilder.cronSchedule(taskInfo.getCron());
        cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(taskInfo.getTriggerName(), taskInfo.getGroupName())
                .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    @Override
    public void init() {
        taskMapper.getTaskInfos().forEach(t -> {
            try {
                newTask(t, getScheduler());
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
    }

    private Scheduler getScheduler() {
        return sfactoryBean.getScheduler();
    }

    @Override
    public Map<String, Object> pause(String jsonStr) {
        Map<String, Object> result = Maps.newHashMap();
        try {
            JsonNode json = Json.parse(jsonStr);
            String taskName = JsonCaseUtil.jsonToString(json.get("taskName"));
            String groupName = JsonCaseUtil.jsonToString(json.get("groupName"));
            // 1.更新 task 任务状态
            getScheduler().pauseJob(new JobKey(taskName, groupName));
            // 2.更新 task 任务数据
            TaskInfo taskInfo = JsonCaseUtil.jsonToBean(jsonStr, TaskInfo.class);
            if (null == taskInfo) {
                result.put("suc", false);
                result.put("msg", "任务暂停，参数错误");
                return result;
            }
            taskInfo.setStatus(0);
            taskInfo.setUpdateTime(new Date());
            taskMapper.updateByTaskNameAndGroupName(taskInfo);
            // 3.返回结果
            logger.info("任务暂停成功");
            result.put("suc", true);
            result.put("msg", "任务暂停成功");
        } catch (Exception e) {
            logger.info("任务暂停异常：{}\n{}", e.getMessage(), e);
            result.put("suc", false);
            result.put("msg", "任务暂停异常");
        }
        return result;
    }

    @Override
    public Map<String, Object> deleteTask(String jsonStr) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("suc", false);
        result.put("msg", "任务删除异常");
        try {
            JsonNode json = Json.parse(jsonStr);
            String taskName = JsonCaseUtil.jsonToString(json.get("taskName"));
            String groupName = JsonCaseUtil.jsonToString(json.get("groupName"));
            // 1.删除 task 任务
            getScheduler().deleteJob(new JobKey(taskName, groupName));
            // 2.删除 task 任务数据
            taskMapper.deleteByTaskNameAndGroupName(taskName, groupName);
            // 3.返回结果
            logger.info("任务删除成功");
            result.put("suc", true);
            result.put("msg", "任务删除成功" );
        } catch (Exception e) {
            logger.error("任务删除异常：{}\n{}", e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Map<String, Object> pauseAllTask() {
        Map<String, Object> result = Maps.newHashMap();
        try {
            // 1.暂停所有 task 任务
            getScheduler().pauseAll();
            // 2.更新 task 所有任务数据
            taskMapper.updateAllTaskStatus(0);
            // 3.返回结果
            result.put("suc", true);
            result.put("msg", "全部暂停成功");
            logger.info("全部暂停成功");
        } catch (Exception e) {
            result.put("suc", false);
            result.put("msg", "全部暂停异常");
            logger.error("全部暂停异常：{}\n{}", e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Object> startAllTask() {
        Map<String, Object> result = Maps.newHashMap();
        try {
            // 1.更新 task 所有任务数据
            taskMapper.updateAllTaskStatus(1);
            // 2.启动所有 task 任务
            init();
            // 3.返回结果
            result.put("suc", true);
            result.put("msg", "全部启动成功");
            logger.info("全部启动成功");
        } catch (Exception e) {
            result.put("suc", false);
            result.put("msg", "全部启动异常");
            logger.error("全部启动异常：{}\n{}", e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Object> updateTask(String jsonStr) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            TaskInfo taskInfo = JsonCaseUtil.jsonToBean(jsonStr, TaskInfo.class);
            String taskName = taskInfo.getTaskName();
            String groupName = taskInfo.getGroupName();
            String cron = taskInfo.getCron();
            String url = taskInfo.getUrl();
            String triggerName = taskInfo.getTriggerName();
            // 根据任务名称和分组名称查询是否存在任务配置
            TaskInfo existTask = taskMapper.selectByTaskNameAndGroupName(taskName, groupName);
            // 如果不存在任务配置，直接返回任务不存在
            if (existTask == null) {
                resultMap.put("suc", false);
                resultMap.put("msg", "任务不存在");
                return resultMap;
            }
            // 如果存在任务配置，查询出任务信息进行判断
            JobKey jobKey = new JobKey(taskName, groupName);
            boolean existJob = getScheduler().checkExists(jobKey);
            if (!existJob) {
                // 如果任务在当前的调度中不存在，则直接更新任务配置
                taskMapper.updateByTaskNameAndGroupName(taskInfo);
            } else {
                /**
                 * 如果任务已经存在调度中，则取出任务信息对比是否有改动
                 */
                JobDetail jobDetail = getScheduler().getJobDetail(jobKey);
                JobDataMap jobDataMap = jobDetail.getJobDataMap();
                String cronOld = jobDataMap.getString("cron");
                String urlOld = jobDataMap.getString("url");
                TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, groupName);
                boolean checkTrigger = getScheduler().checkExists(triggerKey);
                /**
                 * 如果任务时间改变或者触发器名字有变化
                 */
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronOld);
                CronTrigger cronTrigger;
                if ((!StringUtils.isEmpty(cron) && !cronOld.equals(cron))) {
                    scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                    jobDetail.getJobBuilder().usingJobData("cron", cron);
                }
                cronTrigger = TriggerBuilder.newTrigger()
                        .withIdentity(taskInfo.getTriggerName(), taskInfo.getGroupName())
                        .withSchedule(scheduleBuilder).build();
                /**
                 * 如果任务配置的url地址有变动，则跟新job的url地址
                 */
                if (!StringUtils.isEmpty(url) && !urlOld.equals(url)) {
                    jobDetail.getJobBuilder().usingJobData("url", url);
                }
                getScheduler().resumeTrigger(triggerKey);
                getScheduler().rescheduleJob(triggerKey, cronTrigger);
                resultMap.put("suc", true);
                resultMap.put("msg", "更新任务成功");
                /*Trigger.TriggerState triggerState = getScheduler().getTriggerState(triggerKey);

                trigger.getTriggerBuilder().withIdentity(triggerKey).build();
                if (trigger != null || !StringUtils.isEmpty(trigger)) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    cronTrigger.getCronExpression();
                    getScheduler().scheduleJob(jobDetail, trigger);
                    if (triggerState == Trigger.TriggerState.NONE) {

                    }
                }*/

            }
        } catch (Exception e) {
            resultMap.put("suc", false);
            resultMap.put("msg", "更新任务异常");
            logger.error("更新任务异常：{}\n{}", e.getMessage(), e);
            e.printStackTrace();
        }
        return resultMap;
    }
}
