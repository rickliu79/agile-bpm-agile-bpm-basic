package com.dstz.sys.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.sys.core.model.SysScheduleJobLog;

/**
 *
 * @author didi
 */
public interface SysScheduleJobLogManager extends Manager<String, SysScheduleJobLog> {

    /**
     * 选择性插入
     * @param entity
     *          实体
     * @return
     */
    int insertSelective(SysScheduleJobLog entity);

    /**
     * 选择性更新
     *
     * @param entity
     *          更新
     * @return
     */
    int updateByPrimaryKeySelective(SysScheduleJobLog entity);


}
