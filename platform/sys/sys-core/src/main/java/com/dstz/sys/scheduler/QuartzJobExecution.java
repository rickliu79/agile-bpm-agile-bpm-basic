package com.dstz.sys.scheduler;

import com.dstz.sys.core.model.SysScheduleJob;
import org.quartz.JobExecutionContext;

/**
 * quartz任务执行
 *
 * @author didi
 */
public class QuartzJobExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, SysScheduleJob sysScheduleJob)throws Exception {
        JobInvokeUtil.invokeMethod(sysScheduleJob);
    }
}
