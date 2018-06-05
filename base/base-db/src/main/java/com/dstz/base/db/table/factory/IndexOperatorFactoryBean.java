package com.dstz.base.db.table.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dstz.base.db.api.table.IIndexOperator;
import com.dstz.base.db.table.util.SQLConst;

/**
 * IndexOperator factorybean，用户创建IIndexOperator对象。
 *
 * <pre>
 * 配置文件：app-beans.xml
 * &lt;bean id="indexOperator" class="com.dstz.base.db.table.factory.IndexOperatorFactoryBean">
 * 		&lt;property name="dbType" value="${jdbc.dbType}"/>
 * 		&lt;property name="jdbcTemplate" ref="jdbcTemplate"/>
 * &lt;/bean>
 * </pre>
 *
 * @author ray
 */
public class IndexOperatorFactoryBean implements FactoryBean<IIndexOperator> {

    private IIndexOperator indexOperator;

    private String dbType = SQLConst.DB_MYSQL;

    private JdbcTemplate jdbcTemplate;

    @Override
    public IIndexOperator getObject() throws Exception {
        indexOperator = DatabaseFactory.getIndexOperator(dbType);

        indexOperator.setJdbcTemplate(jdbcTemplate);
        return indexOperator;
    }

    /**
     * 设置数据库类型
     *
     * @param dbType
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    /**
     * 设置jdbcTemplate
     *
     * @param jdbcTemplate
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return IIndexOperator.class;
    }

    @Override
    public boolean isSingleton() {

        return true;
    }

}
