package com.dstz.base.db.datasource;

import java.util.HashMap;
import java.util.Map;

import com.dstz.base.core.util.StringUtil;

/**
 * spring数据源，设置当前数据源。<br>
 * <p>
 * 设置方法：<br/>
 *
 * <pre>
 * ApplicationContext c = new ClassPathXmlApplicationContext(locations);
 *
 * DbContextHolder.setDbType(&quot;dataSource_Default2&quot;);
 * manager.addOracle();
 *
 * DbContextHolder.setDefaultDbType();
 * manager.addMysql();
 * </pre>
 */
public class DbContextHolder {
    private static final ThreadLocal<String> contextHolderAlias = new ThreadLocal<String>();
    private static Map<String, String> dataSourceDbType = new HashMap<String, String>();

    /**
     * 设置当前数据库。
     *
     * @param dbAlias :数据源别名
     * @param dbType  ：数据源的类型：oracle,mysql... void
     */
    public static void setDataSource(String dbAlias, String dbType) {
        contextHolderAlias.set(dbAlias);
        DbContextHolder.dataSourceDbType.put(dbAlias, dbType);
    }

    public static void setDefaultDataSource() {
        contextHolderAlias.set(DataSourceUtil.DEFAULT_DATASOURCE);
    }

    /**
     * 取得当前数据源。
     *
     * @return
     */
    public static String getDataSource() {
        String str = (String) contextHolderAlias.get();
        return str == null ? DataSourceUtil.DEFAULT_DATASOURCE : str;
    }

    public static String getDbType() {
        String dataSourceAlias = contextHolderAlias.get();
        if (StringUtil.isEmpty(dataSourceAlias)) {
            dataSourceAlias = DataSourceUtil.DEFAULT_DATASOURCE;
        }

        String str = DbContextHolder.dataSourceDbType.get(contextHolderAlias.get());
        return str;
    }

    /**
     * 清除上下文数据
     */
    public static void clearDataSource() {
        contextHolderAlias.remove();
    }

    /**
     * <pre>
     * 放置一个数据源的数据库类型到dataSourceDbType
     * </pre>
     *
     * @param dsKey  数据源别名
     * @param dbType 数据库类型
     */
    public static void putDataSourceDbType(String dsKey, String dbType) {
        dataSourceDbType.put(dsKey, dbType);
    }

    /**
     * <pre>
     * 从dataSourceDbType获取数据库类型
     * </pre>
     *
     * @param dsKey 数据源别名
     * @return
     */
    public static String getDataSourceDbType(String dsKey) {
        return dataSourceDbType.get(dsKey);
    }
}
