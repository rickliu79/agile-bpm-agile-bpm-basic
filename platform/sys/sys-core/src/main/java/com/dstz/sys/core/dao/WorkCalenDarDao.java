package com.dstz.sys.core.dao;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dstz.base.dao.BaseDao;
import com.dstz.sys.core.model.WorkCalenDar;

/**
 * 
 * <pre> 
 * 描述：c_work_calendar DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:miao
 * 邮箱:miaojifang@ddjf.com.cn
 * 日期:2017-12-26 11:47:55
 * 版权：大道金服
 * </pre>
 */
public interface WorkCalenDarDao extends BaseDao<String, WorkCalenDar> {

	List<WorkCalenDar> getByDay(@Param("day")Date day);
	
	List<WorkCalenDar> getByPeriod(@Param("startDay")Date startDay, @Param("endDay")Date endDay);
	
	Date getWorkDayByDays(@Param("startDay")Date startDay, @Param("days")int days);
	
	List<WorkCalenDar> getByDayAndSystem(@Param("day")Date day, @Param("system")String system);
	
	public void updateWorkType(@Param("startDay")Date startDay, @Param("endDay")Date endDay, String isWorkDay, String type);

	List<WorkCalenDar> getByPeriodAndSystem(@Param("startDay")Date startDay, @Param("endDay")Date endDay, @Param("system")String system);

	Date getWorkDayByDays(@Param("startDay")Date startDay, int i, String system);

	List<WorkCalenDar> getByTimeContainPublic(@Param("startDay")Date startDay, @Param("endDay")Date endDay, @Param("system")String system);
}
