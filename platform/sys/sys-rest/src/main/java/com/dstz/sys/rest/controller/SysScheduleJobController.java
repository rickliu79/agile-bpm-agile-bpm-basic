package com.dstz.sys.rest.controller;

import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.constant.BaseStatusCode;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.db.model.page.PageJson;
import com.dstz.base.rest.GenericController;
import com.dstz.sys.core.manager.SysScheduleJobLogManager;
import com.dstz.sys.core.model.SysScheduleJob;
import com.dstz.sys.core.model.SysScheduleJobLog;
import com.dstz.sys.scheduler.QuartzManagerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 系统任务计划
 *
 * @author didi
 */
@RestController
@RequestMapping("/sys/scheduleJob")
public class SysScheduleJobController extends GenericController {

    private final Logger LOGGER = LoggerFactory.getLogger(SysScheduleJobController.class);

    @Resource
    private QuartzManagerService quartzManagerService;

    @Resource(name = "sysScheduleJobLogManager")
    private SysScheduleJobLogManager sysScheduleJobLogManager;

    /**
     * 查询列表
     */
    @RequestMapping("list")
    public PageJson list(HttpServletRequest request) {
        QueryFilter queryFilter = getQueryFilter(request);
        return new PageJson(quartzManagerService.selectList(queryFilter));
    }

    /**
     * 根据系统执行计划ID得到
     *
     * @param id 执行计划ID
     */
    @RequestMapping("get")
    public ResultMsg<SysScheduleJob> get(@RequestParam("id") String id) {
        SysScheduleJob sysScheduleJob = quartzManagerService.getSysScheduleJobById(id);
        return new ResultMsg<>(sysScheduleJob);
    }

    /**
     * 修改系统执行计划
     *
     * @param sysScheduleJob 执行计划
     * @return
     */
    @PostMapping("edit")
    public ResultMsg<String> edit(@RequestBody SysScheduleJob sysScheduleJob) {
        try {
            if (StringUtils.isEmpty(sysScheduleJob.getId())) {
                quartzManagerService.addSysScheduleJob(sysScheduleJob);
            } else {
                quartzManagerService.updateSysScheduleJob(sysScheduleJob);
            }
            ResultMsg<String> resultMsg = new ResultMsg<>(sysScheduleJob.getId());
            resultMsg.setMsg("操作成功");
            return resultMsg;
        } catch (BusinessException e) {

            return new ResultMsg<>(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new ResultMsg<>(BaseStatusCode.SYSTEM_ERROR, BaseStatusCode.SYSTEM_ERROR.getDesc());
        }
    }

    /**
     * 检查是否存在
     *
     * @param name  计划名称
     * @param group 计划分组
     * @return
     */
    @PostMapping("checkExists")
    public boolean checkExists(@RequestParam(name = "name") String name, @RequestParam("group") String group) {

        return quartzManagerService.checkExists(name, group);
    }

    /**
     * 启用任务计划
     *
     * @param id
     * @param enable
     * @return
     * @throws Exception
     */
    @CatchErr(write2response = true)
    @RequestMapping("enable")
    public ResultMsg<Void> enable(@RequestParam("id") String id, @RequestParam("enable") boolean enable) throws Exception {
        SysScheduleJob sysScheduleJob = quartzManagerService.getSysScheduleJobById(id);
        if (enable) {
            quartzManagerService.enableSysScheduleJob(sysScheduleJob);
        } else {
            quartzManagerService.disableSysScheduleJob(sysScheduleJob);
        }

        return new ResultMsg<>(null);
    }

    /**
     * 立即运行一次
     *
     * @param id 执行计划ID
     * @return
     * @throws Exception
     */
    @RequestMapping("runOnce")
    @CatchErr(write2response = true)
    public ResultMsg<?> runOnce(@RequestParam("id") String id) throws Exception {
        SysScheduleJob sysScheduleJob = quartzManagerService.getSysScheduleJobById(id);
        quartzManagerService.runOnce(sysScheduleJob);
        return new ResultMsg<>();
    }

    /**
     * 移除
     *
     * @param id 执行计划ID
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @CatchErr(write2response = true)
    public ResultMsg<?> remove(@RequestParam("id") String id) throws Exception {
        SysScheduleJob sysScheduleJob = quartzManagerService.getSysScheduleJobById(id);
        quartzManagerService.removeSysScheduleJob(sysScheduleJob);
        return new ResultMsg<>();
    }

    /**
     * 执行计划日志列表
     *
     * @param jobId   任务编号
     * @param request
     * @return
     */
    @RequestMapping("log/list")
    @CatchErr(write2response = true)
    public PageJson listSysScheduleJobLog(@RequestParam("jobId") String jobId, HttpServletRequest request) {
        QueryFilter queryFilter = getQueryFilter(request);
        queryFilter.addFilter("job_id", jobId, QueryOP.EQUAL);
        return new PageJson(sysScheduleJobLogManager.query(queryFilter));
    }

    /**
     * 执行计划详细日志
     * @param id
     *          日志ID
     * @return
     */
    @RequestMapping("log/detail")
    @CatchErr(write2response = true)
    public ResultMsg<?> getLogDetail(@RequestParam("id")String id){
       SysScheduleJobLog sysScheduleJobLog = sysScheduleJobLogManager.get(id);
       return new ResultMsg<>(sysScheduleJobLog);
    }
}
