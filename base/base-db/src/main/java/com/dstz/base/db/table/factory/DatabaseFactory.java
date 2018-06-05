package com.dstz.base.db.table.factory;

import com.dstz.base.db.api.table.IIndexOperator;
import com.dstz.base.db.api.table.ITableOperator;
import com.dstz.base.db.api.table.IViewOperator;
import com.dstz.base.db.table.BaseTableMeta;
import com.dstz.base.db.table.impl.db2.DB2IndexOperator;
import com.dstz.base.db.table.impl.db2.DB2TableMeta;
import com.dstz.base.db.table.impl.db2.DB2TableOperator;
import com.dstz.base.db.table.impl.db2.DB2ViewOperator;
import com.dstz.base.db.table.impl.dm.DmIndexOperator;
import com.dstz.base.db.table.impl.dm.DmTableMeta;
import com.dstz.base.db.table.impl.dm.DmTableOperator;
import com.dstz.base.db.table.impl.dm.DmViewOperator;
import com.dstz.base.db.table.impl.h2.H2IndexOperator;
import com.dstz.base.db.table.impl.h2.H2TableMeta;
import com.dstz.base.db.table.impl.h2.H2TableOperator;
import com.dstz.base.db.table.impl.h2.H2ViewOperator;
import com.dstz.base.db.table.impl.mysql.MySQLIndexOperator;
import com.dstz.base.db.table.impl.mysql.MySQLTableMeta;
import com.dstz.base.db.table.impl.mysql.MySQLTableOperator;
import com.dstz.base.db.table.impl.mysql.MySQLViewOperator;
import com.dstz.base.db.table.impl.oracle.OracleIndexOperator;
import com.dstz.base.db.table.impl.oracle.OracleTableMeta;
import com.dstz.base.db.table.impl.oracle.OracleTableOperator;
import com.dstz.base.db.table.impl.oracle.OracleViewOperator;
import com.dstz.base.db.table.impl.sqlserver.SQLServerIndexOperator;
import com.dstz.base.db.table.impl.sqlserver.SQLServerTableMeta;
import com.dstz.base.db.table.impl.sqlserver.SQLServerTableOperator;
import com.dstz.base.db.table.impl.sqlserver.SQLServerViewOperator;
import com.dstz.base.db.table.util.SQLConst;

/**
 * 元数据读取工厂。
 *
 * @author ray
 */
public class DatabaseFactory {

    public static String EXCEPTION_MSG = "没有设置合适的数据库类型";

    /**
     * 通过数据库类型获得表操作
     *
     * @param dbType 数据库类型
     * @return
     * @throws Exception
     */
    public static ITableOperator getTableOperator(String dbType)
            throws Exception {
        ITableOperator tableOperator = null;
        if (dbType.equals(SQLConst.DB_ORACLE)) {
            tableOperator = new OracleTableOperator();
        } else if (dbType.equals(SQLConst.DB_MYSQL)) {
            tableOperator = new MySQLTableOperator();
        } else if (dbType.equals(SQLConst.DB_SQLSERVER)) {
            tableOperator = new SQLServerTableOperator();
        } else if (dbType.equals(SQLConst.DB_DB2)) {
            tableOperator = new DB2TableOperator();
        } else if (dbType.equals(SQLConst.DB_H2)) {
            tableOperator = new H2TableOperator();
        } else if (dbType.equals(SQLConst.DB_DM)) {
            tableOperator = new DmTableOperator();
        } else {
            throw new Exception(EXCEPTION_MSG);
        }
        return tableOperator;
    }


    /**
     * 通过数据库类型获得表元操作
     *
     * @param dbType 数据库类型
     * @return
     * @throws Exception
     */
    public static BaseTableMeta getTableMetaByDbType(String dbType)
            throws Exception {
        BaseTableMeta meta = null;
        if (dbType.equals(SQLConst.DB_ORACLE)) {
            meta = new OracleTableMeta();
        } else if (dbType.equals(SQLConst.DB_MYSQL)) {
            meta = new MySQLTableMeta();
        } else if (dbType.equals(SQLConst.DB_SQLSERVER)) {
            meta = new SQLServerTableMeta();
        } else if (dbType.equals(SQLConst.DB_DB2)) {
            meta = new DB2TableMeta();
        } else if (dbType.equals(SQLConst.DB_H2)) {
            meta = new H2TableMeta();
        } else if (dbType.equals(SQLConst.DB_DM)) {
            meta = new DmTableMeta();
        } else {
            throw new Exception(EXCEPTION_MSG);
        }
        return meta;
    }

    /**
     * 根据数据类型获取 视图操作
     *
     * @param dbType
     * @return
     * @throws Exception
     */
    public static IIndexOperator getIndexOperator(String dbType)
            throws Exception {
        IIndexOperator indexOperator = null;
        if (dbType.equals(SQLConst.DB_ORACLE)) {
            indexOperator = new OracleIndexOperator();
        } else if (dbType.equals(SQLConst.DB_MYSQL)) {
            indexOperator = new MySQLIndexOperator();
        } else if (dbType.equals(SQLConst.DB_SQLSERVER)) {
            indexOperator = new SQLServerIndexOperator();
        } else if (dbType.equals(SQLConst.DB_DB2)) {
            indexOperator = new DB2IndexOperator();
        } else if (dbType.equals(SQLConst.DB_H2)) {
            indexOperator = new H2IndexOperator();
        } else if (dbType.equals(SQLConst.DB_DM)) {
            indexOperator = new DmIndexOperator();
        } else {
            throw new Exception(EXCEPTION_MSG);
        }
        return indexOperator;

    }

    /**
     * 根据数据源获取 视图操作
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    public static IViewOperator getViewOperator(String dbType)
            throws Exception {

        IViewOperator view = null;
        if (dbType.equals(SQLConst.DB_ORACLE)) {
            view = new OracleViewOperator();
        } else if (dbType.equals(SQLConst.DB_MYSQL)) {
            view = new MySQLViewOperator();
        } else if (dbType.equals(SQLConst.DB_SQLSERVER)) {
            view = new SQLServerViewOperator();
        } else if (dbType.equals(SQLConst.DB_DB2)) {
            view = new DB2ViewOperator();
        } else if (dbType.equals(SQLConst.DB_H2)) {
            view = new H2ViewOperator();
        } else if (dbType.equals(SQLConst.DB_DM)) {
            view = new DmViewOperator();
        } else {
            throw new Exception(EXCEPTION_MSG);
        }

        return view;
    }

}
