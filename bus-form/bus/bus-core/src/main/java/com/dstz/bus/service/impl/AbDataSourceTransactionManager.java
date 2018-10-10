package com.dstz.bus.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.dstz.base.db.datasource.DataSourceUtil;

/**
 * <pre>
 * 描述：ab 结合sys多数据源操作 专门为bo db实例化做的事务管理器
 * 它只保护系统数据源（包含dataSourceDefault），不会保护datasource
 * 其实可以做到，但是这个事务管理器目前只为bo多数据源的保护，所以我没支持
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年10月10日
 * 版权:summer
 * </pre>
 */
public class AbDataSourceTransactionManager extends AbstractPlatformTransactionManager implements ResourceTransactionManager, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.debug("ab的事务管理器已就绪");
	}

	@Override
	public Object getResourceFactory() {
		return DataSourceUtil.getDataSourceByAlias(DataSourceUtil.GLOBAL_DATASOURCE);
	}

	/**
	 * <pre>
	 * 生成一个在整个事务处理都用到的资源
	 * 这里我放了在过程中的所有连接 Map<数据源别名,连接>
	 * </pre>
	 */
	@Override
	protected Object doGetTransaction() {
		return new HashMap<String, Connection>();
	}
	
	/**
	 * <pre>
	 * 准备事务，获取链接
	 * </pre>
	 */
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
		Map<String, Connection> conMap = (Map<String, Connection>) transaction;
		
		Map<String, DataSource> dsMap = DataSourceUtil.getDataSources();
		// 遍历系统中的所有数据源，打开连接
		for (Entry<String, DataSource> entry : dsMap.entrySet()) {
			Connection con = null;
			try {
				ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(entry.getValue());
				if (conHolder == null) {
					con = entry.getValue().getConnection();
					con.setAutoCommit(false);
					// 缓存链接
					TransactionSynchronizationManager.bindResource(entry.getValue(), new ConnectionHolder(con));
				} else {
					con = conHolder.getConnection();
				}
				conMap.put(entry.getKey(), con);
				logger.debug("数据源别名[" + entry.getKey() + "]打开连接成功");
			} catch (Throwable ex) {
				doCleanupAfterCompletion(conMap);
				throw new CannotCreateTransactionException("数据源别名[" + entry.getKey() + "]打开连接错误", ex);
			}
		}

	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		Map<String, Connection> conMap = (Map<String, Connection>) status.getTransaction();
		for (Entry<String, Connection> entry : conMap.entrySet()) {
			try {
				entry.getValue().commit();
				logger.debug("数据源别名[" + entry.getKey() + "]提交事务成功");
			} catch (SQLException ex) {
				doCleanupAfterCompletion(conMap);
				throw new TransactionSystemException("数据源别名[" + entry.getKey() + "]提交事务失败", ex);
			}
		}
	}
	
	/**
	 * 回滚
	 */
	@Override
	protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
		Map<String, Connection> conMap = (Map<String, Connection>) status.getTransaction();
		for (Entry<String, Connection> entry : conMap.entrySet()) {
			try {
				entry.getValue().rollback();
				logger.debug("数据源别名[" + entry.getKey() + "]回滚事务成功");
			} catch (SQLException ex) {
				doCleanupAfterCompletion(conMap);
				throw new TransactionSystemException("数据源别名[" + entry.getKey() + "]回滚事务失败", ex);
			}
		}
	}
	
	/**
	 * 回收链接
	 */
	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		Map<String, Connection> conMap = (Map<String, Connection>) transaction;
		for (Entry<String, Connection> entry : conMap.entrySet()) {
			DataSource dataSource = DataSourceUtil.getDataSourceByAlias(entry.getKey());
			TransactionSynchronizationManager.unbindResource(dataSource);
			DataSourceUtils.releaseConnection(entry.getValue(), dataSource);
			logger.debug("数据源别名[" + entry.getKey() + "]关闭链接成功");
		}
	}
}
