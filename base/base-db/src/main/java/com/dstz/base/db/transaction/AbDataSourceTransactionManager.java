package com.dstz.base.db.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
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
	/**
	 * 线程当前的abTxObject
	 */
	private static ThreadLocal<AbDataSourceTransactionObject> threadLocalAbTxObject = new ThreadLocal<>();
	/**
	 * 线程中是否有活跃的事务
	 */
	private static ThreadLocal<Boolean> transactionActive = new ThreadLocal<>();

	/**
	 * <pre>
	 * 事务是否支持数据库层级别的只读
	 * 则会改变Statement的行为
	 * </pre>
	 */
	private boolean enforceReadOnly = false;

	public AbDataSourceTransactionManager() {
		setNestedTransactionAllowed(true);
	}

	private static Log logger() {
		return AppUtil.getBean(AbDataSourceTransactionManager.class).logger;
	}

	public void setEnforceReadOnly(boolean enforceReadOnly) {
		this.enforceReadOnly = enforceReadOnly;
	}

	public boolean isEnforceReadOnly() {
		return enforceReadOnly;
	}

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
	 * </pre>
	 */
	@Override
	protected Object doGetTransaction() {
		AbDataSourceTransactionObject abTxObject = new AbDataSourceTransactionObject();
		threadLocalAbTxObject.set(abTxObject);
		if (logger().isDebugEnabled()) {
			logger().debug("ab事务编号["+abTxObject.getSerialNumber()+"]开始");
		}
		return abTxObject;
	}

	/**
	 * 判断是否已存在事务
	 */
	@Override
	protected boolean isExistingTransaction(Object transaction) {
		if(transactionActive.get()==null) {
			return false;
		}
		return transactionActive.get();
	}

	/**
	 * <pre>
	 * 准备事务，获取链接
	 * </pre>
	 */
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
		AbDataSourceTransactionObject abTxObject = (AbDataSourceTransactionObject) transaction;
		abTxObject.setDefinition(definition);
		// 先把本地数据源加入管理中
		DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil.getBean(DataSourceUtil.GLOBAL_DATASOURCE);
		addGlobalDataSource(DataSourceUtil.GLOBAL_DATASOURCE, dynamicDataSource);
		transactionActive.set(true);//标记线程已开启了事务
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
		AbDataSourceTransactionObject abTxObject = threadLocalAbTxObject.get();
		try {
			// 拿出dsKey的资源 txObject
			DataSourceTransactionObject txObject = abTxObject.getDsTxObj(dsKey);
			if (txObject == null) {
				txObject = new DataSourceTransactionObject();
				abTxObject.putDsTxObj(dsKey, txObject);
			}

			// 判断资源是否为空，或者资源是否在事务中要同步（则不允许其他事务重复使用）
			if (!txObject.hasConnectionHolder() || txObject.getConnectionHolder().isSynchronizedWithTransaction()) {
				// 尝试在资源中拿
				ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
				if (conHolder != null) {
					// 标记为不是这次管理这次事务中生成的资源，后面回收资源时不回收
					txObject.setConnectionHolder(conHolder, false);
				} else {
					Connection newCon = dataSource.getConnection();
					if (logger().isDebugEnabled()) {
						logger().debug("在ab事务编号["+abTxObject.getSerialNumber()+"]中，"+"数据源别名[" + dsKey + "]打开连接成功");
					}
					// 标记为这次事务中生成的资源，需要回收资源
					txObject.setConnectionHolder(new ConnectionHolder(newCon), true);
				}
			}

			txObject.getConnectionHolder().setSynchronizedWithTransaction(true);
			Connection con = txObject.getConnectionHolder().getConnection();

			TransactionDefinition definition = abTxObject.getDefinition();
			Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
			txObject.setPreviousIsolationLevel(previousIsolationLevel);

			// 设置这个链接必须要被恢复为自动提交，有些数据源的池是有这方面的需要的
			if (con.getAutoCommit()) {
				txObject.setMustRestoreAutoCommit(true);
				if (logger().isDebugEnabled()) {
					logger().debug("在ab事务编号["+abTxObject.getSerialNumber()+"]中，"+"设置数据源别名为[" + dsKey + "]的链接为手动提交(本来是自动提交的)");
				}
				con.setAutoCommit(false);
			}

			prepareTransactionalConnection(con, definition);

			// 设置超时
			int timeout = staticDetermineTimeout(definition);
			if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
				txObject.getConnectionHolder().setTimeoutInSeconds(timeout);
			}

			// 判断是否本次事务生成的新资源，需要绑定到资源中
			if (txObject.isNewConnectionHolder()) {
				TransactionSynchronizationManager.bindResource(dataSource, txObject.getConnectionHolder());
			}
		} catch (Throwable ex) {
			// 释放和关闭这次事务的相关资源
			for (Entry<String, DataSourceTransactionObject> entry : threadLocalAbTxObject.get().getDsTxObjMap().entrySet()) {
				DataSourceTransactionObject txObject = entry.getValue();
				if (txObject.isNewConnectionHolder()) {
					DataSource ds = DataSourceUtil.getDataSourceByAlias(entry.getKey());
					Connection con = txObject.getConnectionHolder().getConnection();
					DataSourceUtils.releaseConnection(con, ds);
					txObject.setConnectionHolder(null, false);
				}
			}
			threadLocalAbTxObject.remove();
			transactionActive.remove();//清除事务标记
			throw new CannotCreateTransactionException("在ab事务编号["+abTxObject.getSerialNumber()+"]中，"+"数据源别名[" + dsKey + "]打开连接错误", ex);
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
		// key跟本地数据源取的真正数据源是一样的且资源中无该ConnectionHolder，则拿出本地数据源的ConnectionHolder绑定到这个数据源中
		if (DbContextHolder.getDataSource().equals(dsKey) && TransactionSynchronizationManager.getResource(dataSource) == null) {
			ConnectionHolder holder = threadLocalAbTxObject.get().getDsTxObj(DataSourceUtil.GLOBAL_DATASOURCE).getConnectionHolder();
			TransactionSynchronizationManager.bindResource(dataSource, holder);
			return;
		}
		addGlobalDataSource(dsKey, dataSource);
	}

	/**
	 * <pre>
	 * 挂起事务，把线程中的ConnectionHolder先移除
	 * return后是会被存储起来的，然后再doResume来还原
	 * 注意，其中的数据库链接是没关闭的哦，只是删除了在线程中的引用
	 * </pre>
	 */
	@Override
	protected Object doSuspend(Object transaction) {
		Map<String, Object> objMap = new HashMap<>();
		AbDataSourceTransactionObject abTxObject = (AbDataSourceTransactionObject) transaction;
		for (Entry<String, DataSourceTransactionObject> entry : abTxObject.getDsTxObjMap().entrySet()) {
			DataSourceTransactionObject txObject = entry.getValue();
			txObject.setConnectionHolder(null);
			DataSource dataSource = DataSourceUtil.getDataSourceByAliasWithLoacl(entry.getKey());
			Object obj = TransactionSynchronizationManager.unbindResource(dataSource);
			objMap.put(entry.getKey(), obj);
			if (logger().isDebugEnabled()) {
				logger().debug("在ab事务编号["+abTxObject.getSerialNumber()+"]中，"+"设置数据源别名为[" + entry.getKey() + "]的资源被挂起");
			}
		}
		return objMap;
	}

	/**
	 * <pre>
	 * 恢复之前挂起的ConnectionHolder
	 * 只需要把资源再次挂上线程中则可，因为dobegin会直接先从线程取
	 * </pre>
	 */
	@Override
	protected void doResume(Object transaction, Object suspendedResources) {
		AbDataSourceTransactionObject abTxObject = (AbDataSourceTransactionObject) transaction;
		Map<String, Object> objMap = (Map<String, Object>) suspendedResources;
		for (Entry<String, Object> entry : objMap.entrySet()) {
			DataSource dataSource = DataSourceUtil.getDataSourceByAliasWithLoacl(entry.getKey());
			TransactionSynchronizationManager.bindResource(dataSource, entry.getValue());
			if (logger().isDebugEnabled()) {
				logger().debug("在ab事务编号["+abTxObject.getSerialNumber()+"]中，"+"设置数据源别名为[" + entry.getKey() + "]的被挂起资源恢复");
			}
		}
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		AbDataSourceTransactionObject abTxObject = (AbDataSourceTransactionObject) status.getTransaction();
		StringBuilder sb = new StringBuilder();
		for (Entry<String, DataSourceTransactionObject> entry : abTxObject.getDsTxObjMap().entrySet()) {
			try {
				entry.getValue().getConnectionHolder().getConnection().commit();
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(entry.getKey());
				logger.debug("在ab事务编号["+abTxObject.getSerialNumber()+"]中，"+"数据源别名[" + entry.getKey() + "]提交事务成功");
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
		AbDataSourceTransactionObject abTxObject = (AbDataSourceTransactionObject) status.getTransaction();
		for (Entry<String, DataSourceTransactionObject> entry : abTxObject.getDsTxObjMap().entrySet()) {
			try {
				entry.getValue().getConnectionHolder().getConnection().rollback();
				logger.debug("在ab事务编号["+abTxObject.getSerialNumber()+"]中，"+"数据源别名[" + entry.getKey() + "]回滚事务成功");
			} catch (SQLException ex) {
				throw new TransactionSystemException("数据源别名[" + entry.getKey() + "]回滚事务失败", ex);
			}
		}
	}

	/**
	 * <pre>
	 * 告诉所有数据源的ConnectionHolder，已被置为回滚状态
	 * </pre>
	 */
	@Override
	protected void doSetRollbackOnly(DefaultTransactionStatus status) {
		AbDataSourceTransactionObject abTxObject = (AbDataSourceTransactionObject) status.getTransaction();
		for (Entry<String, DataSourceTransactionObject> entry : abTxObject.getDsTxObjMap().entrySet()) {
			if (status.isDebug()) {
				logger.debug("在ab事务编号["+abTxObject.getSerialNumber()+"]中，"+"修改数据别名为 [" + entry.getKey() + "]的链接资源为 rollback-only");
			}
			entry.getValue().setRollbackOnly();
		}
	}

	/**
	 * 回收链接ConnectionHolder
	 */
	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		AbDataSourceTransactionObject abTxObject = (AbDataSourceTransactionObject) transaction;
		for (Entry<String, DataSourceTransactionObject> entry : abTxObject.getDsTxObjMap().entrySet()) {
			DataSourceTransactionObject txObject = entry.getValue();
			DataSource dataSource = DataSourceUtil.getDataSourceByAliasWithLoacl(entry.getKey());
			// 如果ConnectionHolder是由本次事务产生，则回收
			if (txObject.isNewConnectionHolder()) {
				TransactionSynchronizationManager.unbindResource(dataSource);
			}

			// 重置链接属性
			Connection con = txObject.getConnectionHolder().getConnection();
			try {
				if (txObject.isMustRestoreAutoCommit()) {
					con.setAutoCommit(true);
				}
				DataSourceUtils.resetConnectionAfterTransaction(con, txObject.getPreviousIsolationLevel());
			} catch (Throwable ex) {
				logger.debug("在ab事务编号["+abTxObject.getSerialNumber()+"]中，"+"在完成事务后，数据源别名为[" + entry.getKey() + "]的属性无法被还原", ex);
			}

			if (txObject.isNewConnectionHolder()) {
				if (logger.isDebugEnabled()) {
					logger.debug("在ab事务编号["+abTxObject.getSerialNumber()+"]中，"+"在完成事务后，释放数据源别名为[" + entry.getKey() + "]的jdbc的链接");
				}
				DataSourceUtils.releaseConnection(con, dataSource);
			}

			txObject.getConnectionHolder().clear();
		}
		// 释放链接刚好是本地数据源的链接复用，而绑定了TransactionSynchronizationManager的资源
		DataSource dataSource = DataSourceUtil.getDataSourceByAlias(DbContextHolder.getDataSource());
		ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
		if (conHolder != null) {
			TransactionSynchronizationManager.unbindResource(dataSource);
		}
		threadLocalAbTxObject.remove();
		transactionActive.remove();//清除事务标记
		if (logger().isDebugEnabled()) {
			logger().debug("ab事务编号["+abTxObject.getSerialNumber()+"]结束");
		}
	}

	/**
	 * <pre>
	 * 处理一下链接为只读链接
	 * </pre>
	 * 
	 * @param con
	 * @param definition
	 * @throws SQLException
	 */
	private static void prepareTransactionalConnection(Connection con, TransactionDefinition definition) throws SQLException {
		boolean isEnforceReadOnly = AppUtil.getBean(AbDataSourceTransactionManager.class).isEnforceReadOnly();
		if (isEnforceReadOnly && definition.isReadOnly()) {
			Statement stmt = con.createStatement();
			try {
				stmt.executeUpdate("SET TRANSACTION READ ONLY");
			} finally {
				stmt.close();
			}
		}
	}

	/**
	 * <pre>
	 * 静态方法取超时时间
	 * </pre>
	 * 
	 * @param definition
	 * @return
	 */
	private static int staticDetermineTimeout(TransactionDefinition definition) {
		if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
			return definition.getTimeout();
		}
		AbDataSourceTransactionManager abTransactionManager = (AbDataSourceTransactionManager) AppUtil.getBean("abTransactionManager");
		return abTransactionManager.getDefaultTimeout();
	}
}
