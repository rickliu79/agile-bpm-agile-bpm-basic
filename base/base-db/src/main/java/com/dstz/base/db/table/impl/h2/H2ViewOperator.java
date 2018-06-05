package com.dstz.base.db.table.impl.h2;

import com.dstz.base.api.Page;
import com.dstz.base.core.util.BeanUtils;
import com.dstz.base.db.api.table.IViewOperator;
import com.dstz.base.db.api.table.model.Column;
import com.dstz.base.db.api.table.model.Table;
import com.dstz.base.db.table.BaseViewOperator;
import com.dstz.base.db.table.colmap.H2ColumnMap;
import com.dstz.base.db.table.model.DefaultTable;
import com.dstz.base.db.table.util.SQLConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * H2 视图操作的实现类。
 *
 * <pre>
 * </pre>
 */
public class H2ViewOperator extends BaseViewOperator implements IViewOperator {

    // private static final String
    // sqlAllView="select view_name from user_views ";
    private static final String SQL_GET_ALL_VIEW = "SELECT " + "TABLE_NAME ,"
            + "REMARKS  " + "FROM " + "INFORMATION_SCHEMA.TABLES " + "WHERE "
            + "TABLE_TYPE = 'VIEW' " + "AND TABLE_SCHEMA=SCHEMA() ";
    private static final String SQL_GET_COLUMNS = "SELECT "
            + "A.TABLE_NAME, "
            + "A.COLUMN_NAME, "
            + "A.IS_NULLABLE, "
            + "A.DATA_TYPE, "
            + "A.CHARACTER_OCTET_LENGTH LENGTH, "
            + "A.NUMERIC_PRECISION PRECISIONS, "
            + "A.NUMERIC_SCALE SCALE, "
            + "B.COLUMN_LIST, "
            + "A.REMARKS "
            + "FROM "
            + "INFORMATION_SCHEMA.COLUMNS A  "
            + "JOIN INFORMATION_SCHEMA.CONSTRAINTS B ON A.TABLE_NAME=B.TABLE_NAME "
            + "WHERE  " + "A.TABLE_SCHEMA=SCHEMA() "
            + "AND UPPER(A.TABLE_NAME)='%s' ";
    static final String SQL_GET_COLUMNS_BATCH = "SELECT "
            + "A.TABLE_NAME, "
            + "A.COLUMN_NAME, "
            + "A.IS_NULLABLE, "
            + "A.DATA_TYPE, "
            + "A.CHARACTER_OCTET_LENGTH LENGTH, "
            + "A.NUMERIC_PRECISION PRECISIONS, "
            + "A.NUMERIC_SCALE SCALE, "
            + "B.COLUMN_LIST, "
            + "A.REMARKS "
            + "FROM "
            + "INFORMATION_SCHEMA.COLUMNS A  "
            + "JOIN INFORMATION_SCHEMA.CONSTRAINTS B ON A.TABLE_NAME=B.TABLE_NAME "
            + "WHERE  " + "A.TABLE_SCHEMA=SCHEMA() ";
    private static final String DB_TYPE = SQLConst.DB_H2;

    /*
     * (non-Javadoc)
     *
     * @see com.dstz.base.api.db.IViewOperator#createOrRep(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void createOrRep(String viewName, String sql) throws Exception {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dstz.base.api.db.IViewOperator#getViews(java.lang.String)
     */
    @Override
    public List<String> getViews(String viewName) throws SQLException {
        String sql = SQL_GET_ALL_VIEW;
        if (StringUtils.isNotEmpty(viewName)) {
            sql += " AND TABLE_NAME LIKE '%" + viewName + "%'";
        }

        RowMapper<String> rowMapper = new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String name = rs.getString("TABLE_NAME");
                return name;
            }
        };
        return this.jdbcTemplate.query(sql, rowMapper);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dstz.base.api.db.IViewOperator#getViews(java.lang.String,
     * com.dstz.base.api.Page)
     */
    @Override
    public List<String> getViews(String viewName, Page page)
            throws SQLException, Exception {
        String sql = SQL_GET_ALL_VIEW;
        if (StringUtils.isNotEmpty(viewName)) {
            sql += " AND TABLE_NAME LIKE '%" + viewName + "%'";
        }

        RowMapper<String> rowMapper = new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String name = rs.getString("TABLE_NAME");
                return name;
            }
        };
        return this.jdbcTemplate.query(sql, rowMapper);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.dstz.base.api.db.IViewOperator#getViewsByName(java.lang.String,
     * com.dstz.base.api.Page)
     */
    @Override
    public List<Table> getViewsByName(String viewName, Page page)
            throws Exception {
        String sql = SQL_GET_ALL_VIEW;
        if (StringUtils.isNotEmpty(viewName)) {
            sql += " AND TABLE_NAME LIKE '%" + viewName + "%'";
        }
        RowMapper<Table> rowMapper = new RowMapper<Table>() {
            @Override
            public Table mapRow(ResultSet rs, int row) throws SQLException {
                Table tableModel = new DefaultTable();
                tableModel.setTableName(rs.getString("table_name"));
                tableModel.setComment(tableModel.getTableName());
                return tableModel;
            }
        };
        List<Table> tableModels = getForList(sql, page, rowMapper, DB_TYPE);
        List<String> tableNames = new ArrayList<String>();
        // get all table names
        for (Table model : tableModels) {
            tableNames.add(model.getTableName());
        }
        // batch get table columns
        Map<String, List<Column>> tableColumnsMap = getColumnsByTableName(tableNames);
        // extract table columns from paraTypeMap by table name;
        for (Entry<String, List<Column>> entry : tableColumnsMap.entrySet()) {
            // set Table's columns
            for (Table model : tableModels) {
                if (model.getTableName().equalsIgnoreCase(entry.getKey())) {
                    model.setColumnList(entry.getValue());
                }
            }
        }
        return tableModels;
    }

    /**
     * 根据表名获取列。此方法使用批量查询方式。
     *
     * @param tableName
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, List<Column>> getColumnsByTableName(
            List<String> tableNames) {
        String sql = SQL_GET_COLUMNS_BATCH;
        Map<String, List<Column>> map = new HashMap<String, List<Column>>();
        if (tableNames != null && tableNames.size() == 0) {
            return map;
        } else {
            StringBuffer buf = new StringBuffer();
            for (String str : tableNames) {
                buf.append("'" + str + "',");
            }
            buf.deleteCharAt(buf.length() - 1);
            sql += " AND A.TABLE_NAME IN (" + buf.toString() + ") ";
        }

        List<Column> columnModels = jdbcTemplate.query(sql, new H2ColumnMap());
        for (Column columnModel : columnModels) {
            String tableName = columnModel.getTableName();
            if (map.containsKey(tableName)) {
                map.get(tableName).add(columnModel);
            } else {
                List<Column> cols = new ArrayList<Column>();
                cols.add(columnModel);
                map.put(tableName, cols);
            }
        }
        return map;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dstz.base.db.table.BaseViewOperator#getType(java.lang.String)
     */
    @Override
    public String getType(String type) {
        type = type.toLowerCase();
        if (type.indexOf("number") > -1)
            return Column.COLUMN_TYPE_NUMBER;
        else if (type.indexOf("date") > -1) {
            return Column.COLUMN_TYPE_DATE;
        } else if (type.indexOf("char") > -1) {
            return Column.COLUMN_TYPE_VARCHAR;
        }
        return Column.COLUMN_TYPE_VARCHAR;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.dstz.base.db.table.BaseViewOperator#getModelByViewName(java.lang
     * .String)
     */
    @Override
    public Table getModelByViewName(String viewName) throws SQLException {
        String sql = SQL_GET_ALL_VIEW;
        sql += " AND UPPER(TABLE_NAME) = '" + viewName.toUpperCase() + "'";
        // Table tableModel= (Table) jdbcTemplate.queryForObject(sql, null,
        // tableModelRowMapper);
        Table tableModel = null;
        List<Table> tableModels = jdbcTemplate.query(sql, tableRowMapper);
        if (BeanUtils.isEmpty(tableModels)) {
            return null;
        } else {
            tableModel = tableModels.get(0);
        }
        // 获取列对象
        List<Column> columnList = getColumnsByTableName(viewName);
        tableModel.setColumnList(columnList);
        return tableModel;
    }

    /**
     * 根据表名获取列
     *
     * @param tableName
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Column> getColumnsByTableName(String tableName) {
        String sql = String.format(SQL_GET_COLUMNS, tableName);
        // jdbcHelper.setCurrentDb(currentDb);

        Map<String, Object> map = new HashMap<String, Object>();
        // sqlColumns语句的column_key包含了column是否为primary
        List<Column> list = jdbcTemplate.query(sql, new H2ColumnMap());
        for (Column model : list) {
            model.setTableName(tableName);
        }
        return list;
    }

    RowMapper<Table> tableRowMapper = new RowMapper<Table>() {
        @Override
        public Table mapRow(ResultSet rs, int row) throws SQLException {
            Table tableModel = new DefaultTable();
            String tabName = rs.getString("TABLE_NAME");
            String tabComment = rs.getString("REMARKS");
            tableModel.setTableName(tabName);
            tableModel.setComment(tabComment);
            return tableModel;
        }
    };

}
