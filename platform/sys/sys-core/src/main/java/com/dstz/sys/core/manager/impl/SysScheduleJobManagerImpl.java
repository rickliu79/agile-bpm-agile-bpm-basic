package com.dstz.sys.core.manager.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dstz.base.manager.impl.BaseManager;
import com.dstz.sys.core.dao.SysScheduleJobDao;
import com.dstz.sys.core.manager.SysScheduleJobManager;
import com.dstz.sys.core.model.SysScheduleJob;

/**
 * 系统执行计划通过处理
 *
 * @author didi
 */
@Service("sysScheduleJobManager")
public class SysScheduleJobManagerImpl extends BaseManager<String, SysScheduleJob> implements SysScheduleJobManager {

    @Resource(name = "sysScheduleJobDao")
    private SysScheduleJobDao sysScheduleJobDao;
 
}
