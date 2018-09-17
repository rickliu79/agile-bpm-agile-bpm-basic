package com.dstz.sys.core.manager;

import java.util.Date;
import java.util.List;

import com.dstz.base.manager.Manager;
import com.dstz.sys.core.model.Schedule;

/**
 * 
 * <pre> 
 * 描述：日程 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:linkai
 * 邮箱:linkai@ddjf.com.cn
 * 日期:2018-02-01 17:45:09
 * 版权：大道金服
 * </pre>
 */
public interface ScheduleManager extends Manager<String, Schedule>{
	
	public List<Schedule> getByPeriodAndOwner(Date start, Date end, String ownerName, String owner);
	
	public void saveSchedule(Schedule schedule);
	
	public List<Schedule> getByPeriodAndSource(Date start, Date end, String source);
	
	public void deleteByBizId(String bizId);
	
	public void dragUpdate(Schedule schedule);
	
	public void updateSchedule(String biz_id, Date start, Date end, String ownerAccount);
	
	public List<Schedule> getByBizId(String biz_id);
	
	public void updateOnlySchedule(Schedule schedule);
	
}
