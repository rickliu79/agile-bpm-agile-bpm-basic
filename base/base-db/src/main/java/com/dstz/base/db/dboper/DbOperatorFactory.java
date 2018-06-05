package com.dstz.base.db.dboper;

import org.springframework.jdbc.core.JdbcTemplate;

import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.PropertyUtil;
import com.dstz.base.db.api.table.DbType;

/**
 * <pre>
 * 描述：DbOperator的工厂类
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年1月22日 下午8:37:04
 * 版权:summer
 * </pre>
 */
public class DbOperatorFactory {
    private DbOperatorFactory() {

    }

    /**
     * <pre>
     * 构建一个操作器
     * </pre>
     *
     * @param type
     * @param jdbcTemplate
     * @return
     */
    public static DbOperator newOperator(String type, JdbcTemplate jdbcTemplate) {
        if (DbType.MYSQL.equalsWithKey(type)) {
            return new MysqlDbOperator(jdbcTemplate);
        }
        return null;
    }
    
    /**
     * <pre>
     * 获取本地数据库操作类
     * </pre>	
     * @return
     */
    public static DbOperator getLocal() {
    	return newOperator(PropertyUtil.getJdbcType(), AppUtil.getBean(JdbcTemplate.class));
    }
}
