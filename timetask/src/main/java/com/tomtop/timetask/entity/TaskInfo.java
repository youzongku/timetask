package com.tomtop.timetask.entity;

import java.util.Date;

import javax.persistence.Table;

/**
 * @author Administrator
 *
 */
@Table(name = "t_task_info")//表名不一致的情况下需要修改
public class TaskInfo {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * group 名称 (/sales/product/member/inventory/purchase)
     */
    private String groupName;

    /**
     * 接口地址
     */
    private String url;

    /**
     * cron 表达式
     */
    private String cron;

    /**
     * 触发名称
     */
    private String triggerName;

    /**
     * 任务状态(0:停止 1:执行中 2:变更)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}