package com.dstz.base.db.table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import com.dstz.base.api.Page;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.dao.CommonDao;
import com.dstz.base.db.api.table.IViewOperator;
import com.dstz.base.db.api.table.model.Column;
import com.dstz.base.db.api.table.model.Table;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.base.db.table.model.DefaultColumn;
import com.dstz.base.db.table.model.DefaultTable;

/**
 * 获取视图信息基类。
 *
 * <pre>
 * </pre>
 */
public abstract class BaseViewOperator extends BaseDbType implements IViewOperator {

    /**
     * 获取数据类型
     *
     * @param type 数据类型
     * @return
     */
    public abstract String getType(String type);

    /**
     * 根据视图名称获取model。
     *
     * @param viewName 视图名
     * @return
     * @throws SQLException
     */
    public Table getModelByViewName(String viewName) throws SQLException {
        Connection conn = jdbcTemplate.getDataSource().getConnection();

        Statement stmt = null;
        ResultSet rs = null;

        Table table = new DefaultTable();
        table.setTableName(viewName);
        table.setComment(viewName);

        try {
            stmt = (Statement) conn.createStatement();
            rs = stmt.executeQuery("select * from " + viewName);
            ResultSetMetaData metadata = rs.getMetaData();
            int count = metadata.getColumnCount();
            // 从第二条记录开始
            for (int i = 1; i <= count; i++) {
                Column column = new DefaultColumn();
                String columnName = metadata.getColumnName(i);
                String typeName = metadata.getColumnTypeName(i);
                String dataType = getType(typeName);
                column.setFieldName(columnName);
                column.setColumnType(dataType);
                column.setComment(columnName);
                table.addColumn(column);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return table;
    }

    /**
     * 获取查询的列表
     *
     * @param sql         查询的SQL
     * @param page        分页
     * @param elementType 返回实体
     * @param dbType      数据库类型
     * @return
     * @throws Exception
     */
    protected <T> List<T> getForList(String sql, Page page,
                                     Class<T> elementType, String dbType) throws Exception {
       if (page != null) {
    	   	CommonDao<T> commonDao = AppUtil.getBean(CommonDao.class);
       	
       		QueryFilter filter = new DefaultQueryFilter();
       		filter.setPage(page);
       		return commonDao.queryForListPage(sql, filter);
        } else {
            return this.jdbcTemplate.queryForList(sql, elementType);
        }
    }

    /**
     * 获取查询的列表
     *
     * @param sql       查询的SQL
     * @param page      分页
     * @param rowMapper 返回行数据
     * @param dbType    数据库类型
     * @return
     * @throws Exception
     */
    protected <T> List<T> getForList(String sql, Page page,RowMapper<T> rowMapper, String dbType) throws Exception {
        if (page != null) {
        	CommonDao<T> commonDao = AppUtil.getBean(CommonDao.class);
        	
        	QueryFilter filter = new DefaultQueryFilter();
        	filter.setPage(page);
        	return commonDao.queryForListPage(sql, filter);
        	
            /*int pageSize = page.getPageSize();
            int offset = page.getStartIndex();
            String pageSql = dialect.getLimitString(sql, offset, pageSize);
            String totalSql = dialect.getCountString(sql);
            List<T> list = this.jdbcTemplate.query(pageSql, rowMapper);
            int total = this.jdbcTemplate.queryForObject(totalSql,Integer.class);
            
            com.github.pagehelper.Page<T> PageList = new com.github.pagehelper.Page<>();
            PageList.addAll(list);
            PageList.setTotal(total);
            PageList.setStartRow(offset);
            PageList.setPageSize(pageSize);
        	return PageList;*/
        } else {
            return this.jdbcTemplate.query(sql, rowMapper);
        }
    }

}
