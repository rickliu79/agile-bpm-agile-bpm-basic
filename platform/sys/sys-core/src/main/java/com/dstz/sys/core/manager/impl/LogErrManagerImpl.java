package com.dstz.sys.core.manager.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.sys.core.dao.LogErrDao;
import com.dstz.sys.core.manager.LogErrManager;
import com.dstz.sys.core.model.LogErr;

/**
 * <pre>
 * 描述：错误日志 处理实现类
 * </pre>
 */
@Service("sysLogErrManager")
public class LogErrManagerImpl extends BaseManager<String, LogErr> implements LogErrManager {
    @Resource
    LogErrDao sysLogErrDao;

    @CatchErr
    @Override
    public void getSub() {
        System.out.println("11111111");
    }
}
