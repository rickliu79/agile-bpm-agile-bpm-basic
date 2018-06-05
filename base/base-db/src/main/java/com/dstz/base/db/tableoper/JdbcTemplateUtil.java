package com.dstz.base.db.tableoper;

import java.sql.Connection;
import java.sql.Statement;

import javax.transaction.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.AppUtil;
/**
 * <pre>
 * 描述：jdbc的工具类
 * ps:
 * 目前主要是为了解决jta事务中ddl语句异常
 * executeWithTransaction
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年5月30日
 * 版权:summer
 * </pre>
 */
public class JdbcTemplateUtil {
	protected static  Logger LOG = LoggerFactory.getLogger(JdbcTemplateUtil.class);
	/**
	 * <pre>
	 * 执行某个sql不需要任何事务保护
	 * ps
	 * 目前主要是为了解决jta事务中ddl语句异常
	 * </pre>
	 * 
	 * @param jdbcTemplate
	 * @param sql
	 */
	public static void executeWithTransaction(JdbcTemplate jdbcTemplate, String sql) {
		JtaTransactionManager jtm = AppUtil.getBean(JtaTransactionManager.class);
		Connection conn = null;
		Statement stat = null;
		Transaction tobj = null;
		try {
			tobj = jtm.getTransactionManager().suspend();//挂起spring的事务管理
			conn = jdbcTemplate.getDataSource().getConnection();
			stat = conn.createStatement();
			stat.execute(sql);
		} catch (Exception e) {
			LOG.error("执行SQL出错【 {}】,错误原因为：{}", sql,e.getMessage(),e);
			throw new BusinessException(e);
		} finally {
			JdbcUtils.closeStatement(stat);
			JdbcUtils.closeConnection(conn);
			if (tobj != null) {
				try {
					jtm.getTransactionManager().resume(tobj);//恢复事务处理
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
