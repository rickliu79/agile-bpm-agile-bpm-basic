package com.dstz.base.db.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.dstz.base.db.datasource.DataSourceUtil;
import com.dstz.base.db.datasource.DbContextHolder;
import com.dstz.base.db.datasource.DynamicDataSource;

/**
 * <pre>
 * 描述：ab 结合sys多数据源操作 专门为bo db实例化做的事务管理器
 * 在该事务管理器只支持PROPAGATION_REQUIRED 的
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年10月10日
 * 版权:summer
 * </pre>
 */
public class AbDataSourceTransactionManager extends AbstractPlatformTransactionManager implements ResourceTransactionManager, InitializingBean {
	private static Logger log = LoggerFactory.getLogger(AbDataSourceTransactionManager.class);
	/**
	 * 线程事务变量
	 */
	private static ThreadLocal<AbDataSourceTransaction> threadLocalTransaction = new ThreadLocal<>();

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
	 * 本人用线程变量来做了
	 * </pre>
	 */
	@Override
	protected Object doGetTransaction() {
		return new HashMap<>();
	}

	/**
	 * 判断是否已存在事务
	 */
	@Override
	protected boolean isExistingTransaction(Object transaction) {
		if (threadLocalTransaction.get() == null) {
			return false;
		}
		return threadLocalTransaction.get().isExist();
	}

	/**
	 * <pre>
	 * 必须实现的一个方法，设置线程内的事务为回滚状态。
	 * 这里其实是为了预防传播性设置为 让线程内可以多次管理器操作的情况下，用来通知大家只做回滚，别commit了。
	 * 在该事务管理器只支持PROPAGATION_REQUIRED 的情况下（线程只有一个管理器操作），没多大用，只是必须要实现这个
	 * 不然抽象类那里会有报错代码。
	 * </pre>
	 */
	@Override
	protected void doSetRollbackOnly(DefaultTransactionStatus status) {
		threadLocalTransaction.get().setRollbackOnly(true);
	}

	/**
	 * <pre>
	 * 准备事务，获取链接
	 * </pre>
	 */
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
		threadLocalTransaction.set(new AbDataSourceTransaction());

		// 先把本地数据源加入管理中
		DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil.getBean(DataSourceUtil.GLOBAL_DATASOURCE);
		addGlobalDataSource(DataSourceUtil.GLOBAL_DATASOURCE, dynamicDataSource);
		threadLocalTransaction.get().setExist(true);// 标记ab事务管理器已经在线程内启动了
	}

	/**
	 * <pre>
	 * 增加一个数据源到线程中
	 * </pre>
	 * 
	 * @param dsKey
	 * @param dataSource
	 */
	private static void addGlobalDataSource(String dsKey, DataSource dataSource) {
		try {
			Connection con = null;
			ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
			if (conHolder == null) {
				con = dataSource.getConnection();
				con.setAutoCommit(false);
				// 缓存链接
				TransactionSynchronizationManager.bindResource(dataSource, new ConnectionHolder(con));
				log.debug("数据源别名[" + dsKey + "]打开连接成功");
			} else {
				con = conHolder.getConnection();
			}
			threadLocalTransaction.get().put(dsKey, con);
		} catch (Throwable ex) {
			doStaticCleanupAfterCompletion();
			throw new CannotCreateTransactionException("数据源别名[" + dsKey + "]打开连接错误", ex);
		}

	}

	/**
	 * <pre>
	 * 增加一个数据源到线程中，并作本地数据源的检查，防止重复打开链接
	 * </pre>
	 * 
	 * @param dsKey
	 * @param dataSource
	 */
	public static void addDataSource(String dsKey, DataSource dataSource) {
		// 系统数据源去重
		DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil.getBean(DataSourceUtil.GLOBAL_DATASOURCE);
		if (dataSource == dynamicDataSource) {
			return;
		}
		// key跟本地数据源取的真正数据源是一样的且资源中无该链接，则拿出本地数据源的链接绑定到这个数据源中
		if (DbContextHolder.getDataSource().equals(dsKey) && TransactionSynchronizationManager.getResource(dataSource) == null) {
			Connection con = threadLocalTransaction.get().getConnMap().get(DataSourceUtil.GLOBAL_DATASOURCE);
			TransactionSynchronizationManager.bindResource(dataSource, new ConnectionHolder(con));
			return;
		}
		addGlobalDataSource(dsKey, dataSource);
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		StringBuilder sb = new StringBuilder();
		Map<String, Connection> conMap = threadLocalTransaction.get().getConnMap();
		for (Entry<String, Connection> entry : conMap.entrySet()) {
			try {
				entry.getValue().commit();
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(entry.getKey());
				logger.debug("数据源别名[" + entry.getKey() + "]提交事务成功");
			} catch (SQLException ex) {
				throw new TransactionSystemException("数据源别名[" + entry.getKey() + "]提交事务失败，需要干预已提交成功的数据源别名[" + sb + "]的数据", ex);
			}
		}
	}

	/**
	 * 回滚
	 */
	@Override
	protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
		Map<String, Connection> conMap = threadLocalTransaction.get().getConnMap();
		for (Entry<String, Connection> entry : conMap.entrySet()) {
			try {
				entry.getValue().rollback();
				logger.debug("数据源别名[" + entry.getKey() + "]回滚事务成功");
			} catch (SQLException ex) {
				throw new TransactionSystemException("数据源别名[" + entry.getKey() + "]回滚事务失败", ex);
			}
		}
	}

	/**
	 * 回收链接
	 */
	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		// 释放线程变量
		doStaticCleanupAfterCompletion();
	}

	private static void doStaticCleanupAfterCompletion() {
		try {
			// 释放线程变量
			DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil.getBean(DataSourceUtil.GLOBAL_DATASOURCE);
			Map<String, Connection> conMap = threadLocalTransaction.get().getConnMap();
			for (Entry<String, Connection> entry : conMap.entrySet()) {
				DataSource dataSource = DataSourceUtil.getDataSourceByAlias(entry.getKey());
				if (DataSourceUtil.GLOBAL_DATASOURCE.equals(entry.getKey())) {
					dataSource = dynamicDataSource;
				}
				TransactionSynchronizationManager.unbindResource(dataSource);
				DataSourceUtils.releaseConnection(entry.getValue(), dataSource);
				log.debug("数据源别名[" + entry.getKey() + "]关闭链接成功");
			}

			// 释放链接刚好是本地数据源的链接复用，而绑定了TransactionSynchronizationManager的资源
			DataSource dataSource = DataSourceUtil.getDataSourceByAlias(DbContextHolder.getDataSource());
			ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
			if (conHolder != null) {
				TransactionSynchronizationManager.unbindResource(dataSource);
			}
		} finally {
			threadLocalTransaction.remove();// 释放资源
		}
	}

}
