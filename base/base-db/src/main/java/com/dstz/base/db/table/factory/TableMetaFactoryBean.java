package com.dstz.base.db.table.factory;

import com.dstz.base.db.table.BaseTableMeta;
import com.dstz.base.db.table.util.SQLConst;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * TableOperator factorybean，用户创建ITableOperator对象。
 *
 * <pre>
 * 配置文件：app-beans.xml
 * &lt;bean id="tableOperator" class="com.dstz.base.db.table.factory.TableOperatorFactoryBean">
 * 		&lt;property name="dbType" value="${jdbc.dbType}"/>
 * 		&lt;property name="jdbcTemplate" ref="jdbcTemplate"/>
 * &lt;/bean>
 * </pre>
 *
 * @author ray
 */
public class TableMetaFactoryBean implements FactoryBean<BaseTableMeta> {

    private BaseTableMeta tableMeta;

    // 数据库类型
    private String dbType = SQLConst.DB_MYSQL;
    // 数据源
    private JdbcTemplate jdbcTemplate;

    @Override
    public BaseTableMeta getObject() throws Exception {

        tableMeta = DatabaseFactory.getTableMetaByDbType(dbType);
        tableMeta.setJdbcTemplate(jdbcTemplate);
        return tableMeta;
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
     * @param 设置数据源
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Class<?> getObjectType() {
        return BaseTableMeta.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
