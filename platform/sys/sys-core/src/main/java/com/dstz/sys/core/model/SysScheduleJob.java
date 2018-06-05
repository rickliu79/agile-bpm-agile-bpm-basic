package com.dstz.sys.core.model;

import com.dstz.base.api.model.IBaseModel;

import java.util.Date;

/**
 *  系统执行计划
 *
 * @author didi
 */
public class SysScheduleJob implements IBaseModel {

    private static final long serialVersionUID = 3362400754730125522L;

    /**
     * 主键编号
     */
    private String id;

    /**
     * 任务计划名称
     */
    private String name;

    /**
     * 任务计划分组
     */
    private String group;

    /**
     * 任务计划说明
     */
    private String description;

    /**
     * 调用目标
     */
    private String invokeTarget;

    /**
     * 运行表达式
     */
    private String cronExpression;

    /**
     * 运行状态
     */
    private String runningState;

    /**
     * 是否并发执行
     */
    private Boolean isConcurrent;

    /**
     * 创建用户
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改用户
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 有效记录
     */
    private Boolean deleteFlag;

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {

        return this.id;
    }

    @Override
    public String getCreateBy() {

        return this.createBy;
    }

    @Override
    public void setCreateBy(String createBy) {

        this.createBy = createBy;
    }

    @Override
    public Date getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {

        return this.updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getUpdateBy() {

        return this.updateBy;
    }

    @Override
    public void setUpdateBy(String updateBy) {

        this.updateBy = updateBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvokeTarget() {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget) {
        this.invokeTarget = invokeTarget;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getRunningState() {
        return runningState;
    }

    public void setRunningState(String runningState) {
        this.runningState = runningState;
    }

    public Boolean getConcurrent() {
        return isConcurrent;
    }

    public void setConcurrent(Boolean concurrent) {
        isConcurrent = concurrent;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}