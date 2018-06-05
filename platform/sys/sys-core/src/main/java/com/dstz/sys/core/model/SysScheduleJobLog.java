package com.dstz.sys.core.model;

import com.dstz.base.api.model.IBaseModel;

import java.util.Date;

/**
 * 任务计划日志
 * @author didi
 */
public class SysScheduleJobLog implements IBaseModel {

    private static final long serialVersionUID = 8829250624466439265L;

    /**
     * 编号
     */
    private String id;

    /**
     * 任务计划ID
     */
    private String jobId;

    /**
     * 执行状态
     */
    private String runState;

    /**
     * 运行毫秒
     */
    private Long runMs;

    /**
     * 运行内容
     */
    private String content;

    /**
     * 运行启动时间
     */
    private Date startTime;

    /**
     * 运行结束时间
     */
    private Date endTime;

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
     * 有效记录 0 正常 1 已删除
     */
    private Boolean deleteFlag;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getRunState() {
        return runState;
    }

    public void setRunState(String runState) {
        this.runState = runState;
    }

    public Long getRunMs() {
        return runMs;
    }

    public void setRunMs(Long runMs) {
        this.runMs = runMs;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getUpdateBy() {
        return updateBy;
    }

    @Override
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}