package com.dstz.base.db.table.util;

import org.springframework.jdbc.core.JdbcTemplate;

import com.dstz.base.core.util.AppUtil;
import com.dstz.base.db.api.table.IViewOperator;
import com.dstz.base.db.table.BaseTableMeta;
import com.dstz.base.db.table.factory.DatabaseFactory;

public class MetaDataUtil {

    /**
     * 获取一个BaseTableMeta，已经设置好方言和jdbcTemplate
     *
     * @param dbType
     * @return BaseTableMeta
     * @throws
     * @since 1.0.0
     */
    public static BaseTableMeta getBaseTableMetaAfterSetDT(String dbType) {
        JdbcTemplate jdbcTemplate = AppUtil.getBean(JdbcTemplate.class);
        BaseTableMeta baseTableMeta = null;
        try {
            baseTableMeta = DatabaseFactory.getTableMetaByDbType(dbType);

            /**
             * 配置文件中的对象
             *
             * @Resource JdbcTemplate jdbcTemplate;
             */
            baseTableMeta.setJdbcTemplate(jdbcTemplate);
        } catch (Exception e) {
        }
        return baseTableMeta;
    }

    /**
     * 获取一个IViewOperator，已经设置好方言和jdbcTemplate
     *
     * @param dbType
     * @return IViewOperator
     * @throws
     * @since 1.0.0
     */
    public static IViewOperator getIViewOperatorAfterSetDT(String dbType) {
        JdbcTemplate jdbcTemplate = AppUtil.getBean(JdbcTemplate.class);
        IViewOperator iViewOperator = null;
        try {
            iViewOperator = DatabaseFactory.getViewOperator(dbType);

            /**
             * 配置文件中的对象
             *
             * @Resource JdbcTemplate jdbcTemplate;
             */
            iViewOperator.setJdbcTemplate(jdbcTemplate);

        } catch (Exception e) {
        }

        return iViewOperator;
    }
}
