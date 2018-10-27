package com.dstz.base.db.transaction;
/**
 * <pre>
 * 描述：ab事务管理器的事务对象
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年10月17日
 * 版权:summer
 * </pre>
 */

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.dstz.base.db.datasource.DataSourceUtil;

public class AbDataSourceTransaction {
	/**
	 * 线程中是否已存在事务
	 */
	private boolean exist;
	/**
	 * 线程中的事务是否处于回滚状态
	 */
	private boolean rollbackOnly;
	/**
	 * 线程中的事务中的所有的数据源链接 Map<数据源别名,链接>
	 */
	private Map<String, Connection> connMap = new LinkedHashMap<>();

	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	public boolean isRollbackOnly() {
		return rollbackOnly;
	}

	public void setRollbackOnly(boolean rollbackOnly) {
		this.rollbackOnly = rollbackOnly;
	}

	public Map<String, Connection> getConnMap() {
		return connMap;
	}
	
	/**
	 * <pre>
	 * 需求的put
	 * 需要保证本地数据源是最后一个链接
	 * 因为commit时（或者说其他操作），需要最复杂，操作最多的本地数据源保持强一致性
	 * </pre>	
	 * @param dsKey
	 * @param conn
	 */
	public void put(String dsKey,Connection conn) {
		connMap.put(dsKey, conn);
		Connection con = connMap.remove(DataSourceUtil.GLOBAL_DATASOURCE);
		connMap.put(DataSourceUtil.GLOBAL_DATASOURCE, con);
	}
	
	
}
