package com.dstz.bus.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.ThreadMapUtil;
import com.dstz.base.db.datasource.DataSourceUtil;
import com.dstz.base.db.datasource.DbContextHolder;
import com.dstz.base.db.datasource.DynamicDataSource;

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
	/**
	 * 事务标记为回滚的标记
	 */
	private static final String AB_TRANSACTION_MANAGER_ROLLBACK_ONLY = "abTransactionManagerRollbackOnly";
	/**
	 * 事务已在线程内存在的标记
	 */
	public static final String AB_TRANSACTION_MANAGER_EXIST = "abTransactionManagerExist";
	/**
	 * 分布式事务管理器需要保护的数据库keys
	 */
	public static final String AB_TRANSACTION_MANAGER_DATASOURCE_KEYS = "abTransactionManagerDataSourceKeys";
	private int i = 0;

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
	 * 判断是否已存在事务
	 */
	@Override
	protected boolean isExistingTransaction(Object transaction) {
		return (boolean) ThreadMapUtil.getOrDefault(AB_TRANSACTION_MANAGER_EXIST, false);
	}

	/**
	 * <pre>
	 * 必须实现的一个方法，设置线程内的事务为回滚状态。
	 * 这里其实是为了预防传播性设置为 让线程内可以多次管理器操作的情况下，用来通知大家不要只做回滚，别commit了。
	 * 在该事务管理器只支持PROPAGATION_REQUIRED 的情况下（线程只有一个管理器操作），没多大用，只是必须要实现这个
	 * 不然抽象类那里会有报错代码。
	 * </pre>
	 */
	@Override
	protected void doSetRollbackOnly(DefaultTransactionStatus status) {
		ThreadMapUtil.put(AB_TRANSACTION_MANAGER_ROLLBACK_ONLY, true);// 标记ab事务管理器在线程内已准备要回滚了
	}

	/**
	 * <pre>
	 * 准备事务，获取链接
	 * </pre>
	 */
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
		logger.info("分布式事务开始:" + i);

		Set<String> dsKeys = (Set<String>) ThreadMapUtil.get(AB_TRANSACTION_MANAGER_DATASOURCE_KEYS);
		Map<String, Connection> conMap = (Map<String, Connection>) transaction;
		Map<String, DataSource> dsMap = DataSourceUtil.getDataSources();
		// 遍历系统中的所有数据源，打开连接
		for (Entry<String, DataSource> entry : dsMap.entrySet()) {
			if (!dsKeys.contains(entry.getKey())) {// 跳过不需要保护的数据源别名
				continue;
			}

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

		/**
		 * <pre>
		 * 处理本地数据源。
		 * 其逻辑，是如果本地数据源已有链接放到conMap中；如果没有，则新建链接，把key为GLOBAL_DATASOURCE放到conMap中
		 * 然后再把数据源链接绑定到TransactionSynchronizationManager中
		 * </pre>
		 */
		Connection con = conMap.get(DbContextHolder.getDataSource());
		DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil.getBean(DataSourceUtil.GLOBAL_DATASOURCE);
		if (con == null) {
			try {
				con = dynamicDataSource.getConnection();
				con.setAutoCommit(false);
				logger.debug("数据源别名[" + DbContextHolder.getDataSource() + "]打开连接成功");
			} catch (SQLException e) {
				doCleanupAfterCompletion(conMap);
				throw new CannotCreateTransactionException("数据源别名[" + DbContextHolder.getDataSource() + "]打开连接错误", e);
			}
			conMap.put(DataSourceUtil.GLOBAL_DATASOURCE, con);
		}
		TransactionSynchronizationManager.bindResource(dynamicDataSource, new ConnectionHolder(con));

		ThreadMapUtil.put(AB_TRANSACTION_MANAGER_EXIST, true);// 标记ab事务管理器已经在线程内启动了
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
		logger.info("分布式事务提交:" + i);
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
		logger.info("分布式事务回滚:" + i);
	}

	/**
	 * 回收链接
	 */
	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		// 释放线程变量
		ThreadMapUtil.remove(AB_TRANSACTION_MANAGER_EXIST);
		ThreadMapUtil.remove(AB_TRANSACTION_MANAGER_ROLLBACK_ONLY);
		ThreadMapUtil.remove(AB_TRANSACTION_MANAGER_DATASOURCE_KEYS);
		
		DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil.getBean(DataSourceUtil.GLOBAL_DATASOURCE);
		Map<String, Connection> conMap = (Map<String, Connection>) transaction;
		for (Entry<String, Connection> entry : conMap.entrySet()) {
			DataSource dataSource = DataSourceUtil.getDataSourceByAlias(entry.getKey());
			if (DataSourceUtil.GLOBAL_DATASOURCE.equals(entry.getKey())) {
				dataSource = dynamicDataSource;
			}
			TransactionSynchronizationManager.unbindResource(dataSource);
			DataSourceUtils.releaseConnection(entry.getValue(), dataSource);
			logger.debug("数据源别名[" + entry.getKey() + "]关闭链接成功");
		}
		
		if(!conMap.containsKey(DataSourceUtil.GLOBAL_DATASOURCE)) {//处理一下本地数据源
			TransactionSynchronizationManager.unbindResource(dynamicDataSource);
		}
		
		logger.info("分布式事务释放:" + (i++));
	}

}
