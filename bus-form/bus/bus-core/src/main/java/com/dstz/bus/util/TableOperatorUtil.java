package com.dstz.bus.util;

import com.dstz.base.db.api.table.DbType;
import com.dstz.base.db.datasource.DataSourceUtil;
import com.dstz.base.db.tableoper.MysqlTableOperator;
import com.dstz.base.db.tableoper.TableOperator;
import com.dstz.bus.model.BusinessTable;
import org.springframework.jdbc.core.JdbcTemplate;

public class TableOperatorUtil {

    /**
     * 从当前可用的数据源中 加工 tableOperator<br>
     * TODO 如果不存在动态数据源中。也不可用。那么不支持获取吧busData
     *
     * @param dsKey
     * @param table
     * @return
     */
    public static TableOperator newOperator(BusinessTable table) {
        JdbcTemplate jdbcTemplate = DataSourceUtil.getJdbcTempByDsAlias(table.getDsKey());
        if (jdbcTemplate == null) {
            throw new RuntimeException("当前系统不存在的数据源:" + table.getDsKey());
        }

        String type = DbType.MYSQL.getKey();// DataSourceUtil 已经放了所有数据源。能不能直接从缓存中获取一下数据源的 类型

        if (DbType.MYSQL.equalsWithKey(type)) {
            return new MysqlTableOperator(table, jdbcTemplate);
        }

        throw new RuntimeException("找不到类型[" + type + "]的数据库处理者(TableOperator)");
    }
}
