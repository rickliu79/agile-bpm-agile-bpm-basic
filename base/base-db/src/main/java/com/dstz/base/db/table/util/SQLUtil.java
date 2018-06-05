package com.dstz.base.db.table.util;

import org.apache.commons.lang3.StringUtils;

/**
 * SQl的帮助类
 *
 * <pre>
 * </pre>
 */
public class SQLUtil {

    /**
     * 根据JDBC连接URL，取得数据库类型
     *
     * @param Driver
     * @return
     */
    public static String getDbTypeByUrl(String url) {
        if (StringUtils.isEmpty(url))
            return null;
        if (url.startsWith("jdbc:derby:")) {
            return SQLConst.DB_DERBY;
        } else if (url.startsWith("jdbc:mysql:")) {
            return SQLConst.DB_MYSQL;
        } else if (url.startsWith("jdbc:oracle:")) {
            return SQLConst.DB_ORACLE;
        } else if (url.startsWith("jdbc:microsoft:")
                || url.startsWith("jdbc:sqlserver:")) {
            return SQLConst.DB_SQLSERVER;
        } else if (url.startsWith("jdbc:sybase:Tds:")) {
            return SQLConst.DB_SYBASE;
        } else if (url.startsWith("jdbc:jtds:")) {
            return SQLConst.DB_JTDS;
        } else if (url.startsWith("jdbc:fake:") || url.startsWith("jdbc:mock:")) {
            return SQLConst.DB_MOCK;
        } else if (url.startsWith("jdbc:postgresql:")) {
            return SQLConst.DB_POSTGRESQL;
        } else if (url.startsWith("jdbc:hsqldb:")) {
            return SQLConst.DB_HSQL;
        } else if (url.startsWith("jdbc:db2:")) {
            return SQLConst.DB_DB2;
        } else if (url.startsWith("jdbc:sqlite:")) {
            return SQLConst.DB_SQLITE;
        } else if (url.startsWith("jdbc:ingres:")) {
            return SQLConst.DB_INGRES;
        } else if (url.startsWith("jdbc:h2:")) {
            return SQLConst.DB_H2;
        } else if (url.startsWith("jdbc:mckoi:")) {
            return SQLConst.DB_MCKOI;
        } else if (url.startsWith("jdbc:cloudscape:")) {
            return SQLConst.DB_CLOUDSCAPE;
        } else if (url.startsWith("jdbc:informix-sqli:")) {
            return SQLConst.DB_INFORMIX;
        } else if (url.startsWith("jdbc:timesten:")) {
            return SQLConst.DB_TIMESTEN;
        } else if (url.startsWith("jdbc:as400:")) {
            return SQLConst.DB_AS400;
        } else if (url.startsWith("jdbc:sapdb:")) {
            return SQLConst.DB_SAPDB;
        } else if (url.startsWith("jdbc:JSQLConnect:")) {
            return SQLConst.DB_JSQLCONNECT;
        } else if (url.startsWith("jdbc:JTurbo:")) {
            return SQLConst.DB_JTURBO;
        } else if (url.startsWith("jdbc:firebirdsql:")) {
            return SQLConst.DB_FIREBIRDSQL;
        } else if (url.startsWith("jdbc:interbase:")) {
            return SQLConst.DB_INTERBASE;
        } else if (url.startsWith("jdbc:pointbase:")) {
            return SQLConst.DB_POINTBASE;
        } else if (url.startsWith("jdbc:edbc:")) {
            return SQLConst.DB_EDBC;
        } else if (url.startsWith("jdbc:mimer:multi1:")) {
            return SQLConst.DB_MIMER;
        } else if (url.startsWith("jdbc:dm:")) {
            return SQLConst.DB_DM;
        } else {
            return null;
        }
    }

    /**
     * 根据JDBC连接driver，取得数据库类型
     *
     * @param driver
     * @return
     */
    public static String getDbTypeByDriver(String driver) {
        if (StringUtils.isEmpty(driver))
            return null;
        if (driver.contains(SQLConst.DB_ORACLE)) {// oracle.jdbc.OracleDriver
            return SQLConst.DB_ORACLE;
        } else if (driver.contains(SQLConst.DB_SQLSERVER)) {// com.microsoft.sqlserver.jdbc.SQLServerDriver
            return SQLConst.DB_SQLSERVER;
        } else if (driver.contains(SQLConst.DB_DB2)) {// com.ibm.db2.jcc.DB2Driver
            return SQLConst.DB_DB2;
        } else if (driver.contains(SQLConst.DB_MYSQL)) {// com.mysql.jdbc.Driver
            return SQLConst.DB_MYSQL;
        } else if (driver.contains(SQLConst.DB_H2)) {// org.h2.Driver
            return SQLConst.DB_H2;
        } else if (driver.contains(SQLConst.DB_DM)) {// dm.jdbc.driver.DmDriver
            return SQLConst.DB_DM;
        }
        return null;
    }

}
