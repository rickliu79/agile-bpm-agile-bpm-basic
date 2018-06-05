package com.dstz.sys.core.manager.impl;

import com.dstz.base.manager.impl.BaseManager;
import com.dstz.sys.core.dao.SysScheduleJobLogDao;
import com.dstz.sys.core.manager.SysScheduleJobLogManager;
import com.dstz.sys.core.model.SysScheduleJobLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author didi
 */
@Service("sysScheduleJobLogManager")
public class SysScheduleJobLogManagerImpl extends BaseManager<String, SysScheduleJobLog> implements SysScheduleJobLogManager {

    @Resource
    private SysScheduleJobLogDao sysScheduleJobLogDao;

    @Override
    public int insertSelective(SysScheduleJobLog entity) {

        return sysScheduleJobLogDao.insertSelective(entity);
    }

    @Override
    public int updateByPrimaryKeySelective(SysScheduleJobLog entity) {

        return sysScheduleJobLogDao.updateByPrimaryKeySelective(entity);
    }
}
