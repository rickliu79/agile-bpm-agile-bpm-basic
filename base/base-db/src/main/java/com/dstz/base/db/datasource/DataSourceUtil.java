package com.dstz.base.db.datasource;

import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 数据源工具。 可以动态添加删除数据源。
 */
public class DataSourceUtil {

    public static final String GLOBAL_DATASOURCE = "dataSource";

    public static final String DEFAULT_DATASOURCE = "dataSourceDefault";

    public static final String TARGET_DATASOURCES = "targetDataSources";

    /**
     * 添加数据源 。
     *
     * @param key
     * @param dataSource void
     * @param replace    若存在是否替代
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void addDataSource(String key, DataSource dataSource, boolean replace) {
        DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil.getBean(GLOBAL_DATASOURCE);
        if (dynamicDataSource.isDataSourceExist(key)) {
            if (!replace)
                return;
            dynamicDataSource.removeDataSource(key);
        }
        dynamicDataSource.addDataSource(key, dataSource);
    }

    /**
     * <pre>
     * 添加数据源 。
     * </pre>
     *
     * @param key
     * @param dataSource
     * @param dbType     数据源类型，填写后会放置到DbContextHolder.dataSourceDbType中
     * @param replace
     */
    public static void addDataSource(String key, DataSource dataSource, String dbType, boolean replace) {
        addDataSource(key, dataSource, replace);
        if (StringUtil.isNotEmpty(dbType)) {
            DbContextHolder.putDataSourceDbType(key, dbType);
        }
    }

    /**
     * <pre>
     * 判断在环境中是否已存在别名key的数据源
     * </pre>
     *
     * @param key
     * @return
     */
    public static boolean isDataSourceExist(String key) {
        DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil.getBean(GLOBAL_DATASOURCE);
        return dynamicDataSource.isDataSourceExist(key);
    }

    /**
     * 根据名字删除数据源。
     *
     * @param key void
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void removeDataSource(String key) throws IllegalAccessException, NoSuchFieldException {
        DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil.getBean(GLOBAL_DATASOURCE);
        dynamicDataSource.removeDataSource(key);
    }

    /**
     * 取得数据源。
     *
     * @return Map&lt;String,DataSource>
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Map<String, DataSource> getDataSources() {
        DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil.getBean(GLOBAL_DATASOURCE);
        return dynamicDataSource.getDataSource();

    }

    /**
     * 根据别名返回容器里对应的数据源
     *
     * @param alias
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException   DataSource
     */
    private static DataSource getDataSourceByAlias(String alias) {
        Map<String, DataSource> map = getDataSources();
        return map.get(alias);
    }

    /**
     * 根据数据源别名返回容器里对应的jdbctemp
     *
     * @param alias
     * @return
     * @throws Exception JdbcTemplate
     * @throws
     * @since 1.0.0
     */
    public static JdbcTemplate getJdbcTempByDsAlias(String alias) {
        if (alias.equals(DEFAULT_DATASOURCE)) {
            return (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
        }
        return new JdbcTemplate(DataSourceUtil.getDataSourceByAlias(alias));
    }

}
