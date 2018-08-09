package com.dstz.base.db.table.impl.mysql;

import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.api.table.ITableOperator;
import com.dstz.base.db.api.table.model.Index;
import com.dstz.base.db.table.BaseIndexOperator;
import com.dstz.base.db.table.model.DefaultIndex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MySQL 索引操作的实现
 *
 * <pre>
 * </pre>
 */
public class MySQLIndexOperator extends BaseIndexOperator {

    // 批量操作的
    protected int BATCH_SIZE = 100;

    /*
     * (non-Javadoc)
     *
     * @see com.dstz.base.db.table.BaseIndexOperator#createIndex(java.lang.String, java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public void createIndex(Index index) throws SQLException {
        String sql = genIndexDDL(index);
        jdbcTemplate.execute(sql);
        index.setIndexDdl(sql);
    }

    /**
     * 生成Index对象对应的DDL语句
     *
     * @param index
     * @return
     */
    private String genIndexDDL(Index index) {
        StringBuffer ddl = new StringBuffer();
        ddl.append("CREATE");
        if (StringUtil.isNotEmpty(index.getIndexType())) {
            ddl.append(" " + index.getIndexType() + " ");
        }
        ddl.append(" INDEX");
        ddl.append(" " + index.getIndexName());
        ddl.append(" ON " + index.getTableName());
        ddl.append("(");
        for (String column : index.getColumnList()) {
            ddl.append(column + ",");
        }
        if (!StringUtils.isEmpty(index.getIndexComment())) {
            ddl.append("COMMENT '" + index.getIndexComment() + "'");
        }
        ddl.replace(ddl.length() - 1, ddl.length(), ")");
        return ddl.toString();
    }

    /*
     * (non-Javadoc) 刪除索引
     *
     * @see com.dstz.base.db.table.BaseIndexOperator#dropIndex(java.lang.String, java.lang.String)
     */
    @Override
    public void dropIndex(String tableName, String indexName) {
        String sql = "drop index " + indexName + " on " + tableName;
        jdbcTemplate.execute(sql);
    }

    /**
     * 通过SQL获得索引
     *
     * @param sql
     * @return
     */
    private List<Index> getIndexesBySql(String sql) {
        List<Index> indexes = jdbcTemplate.query(sql, new RowMapper<Index>() {
            @Override
            public Index mapRow(ResultSet rs, int rowNum) throws SQLException {
                Index index = new DefaultIndex();
                List<String> columns = new ArrayList<String>();
                index.setTableName(rs.getString("TABLE_NAME"));
                index.setIndexName(rs.getString("INDEX_NAME"));
                index.setIndexType(rs.getString("INDEX_TYPE"));
                index.setUnique(rs.getInt("NON_UNIQUE") == 0 ? true : false);
                // index.setIndexComment(rs.getString("INDEX_COMMENT"));
                columns.add(rs.getString("COLUMN_NAME"));
                index.setColumnList(columns);
                return index;
            }

        });
        return indexes;

    }

    /**
     * indexes中，每一索引列，对就一元素。需要进行合并。
     *
     * @param indexes
     * @return
     */
    private List<Index> mergeIndex(List<Index> indexes) {
        List<Index> indexList = new ArrayList<Index>();
        for (Index index : indexes) {
            boolean found = false;
            for (Index index1 : indexList) {
                if (index.getIndexName().equals(index1.getIndexName()) && index.getTableName().equals(index1.getTableName())) {
                    index1.getColumnList().add(index.getColumnList().get(0));
                    found = true;
                    break;
                }
            }
            if (!found)
                indexList.add(index);
        }
        return indexList;
    }

    /**
     * 判断索引是否是主键索引。如果是，则将索引index的pkIndex属性设置为true。此方法是批量操作，主要是减少对数据库访问次数。
     *
     * @param indexList
     * @return
     * @throws SQLException
     */
    private List<Index> dedicatePKIndex(List<Index> indexList) throws SQLException {
        // 用于存放所有索引所包含的所有数据表名(去掉重复)
        List<String> tableNames = new ArrayList<String>();
        for (Index index : indexList) {
            // 将索引对应的表名放到tableNames中。
            if (!tableNames.contains(index.getTableName())) {
                tableNames.add(index.getTableName());
            }
        }
        Map<String, List<String>> tablePKColsMaps = getTablesPKColsByNames(tableNames);
        for (Index index : indexList) {
            if (isListEqual(index.getColumnList(), tablePKColsMaps.get(index.getTableName()))) {
                index.setPkIndex(true);
            } else {
                index.setPkIndex(false);
            }
        }

        return indexList;
    }

    /**
     * 根据表名，取得对应表的主键列。此方法是指操作，主要是减少对数据库的访问次数。返回的Map中：key=表名；value=表名对应的主键列列表。
     *
     * @param tableNames
     * @return
     * @throws SQLException
     */
    private Map<String, List<String>> getTablesPKColsByNames(List<String> tableNames) throws SQLException {
        Map<String, List<String>> tableMaps = new HashMap<String, List<String>>();
        List<String> names = new ArrayList<String>();
        for (int i = 1; i <= tableNames.size(); i++) {
            names.add(tableNames.get(i - 1));
            if (i % BATCH_SIZE == 0 || i == tableNames.size()) {
                Map<String, List<String>> map;
                map = getPKColumns(names);
                tableMaps.putAll(map);
                names.clear();
            }
        }
        return tableMaps;
    }

    /**
     * 获取主键字段
     *
     * @param tableNames
     * @return
     * @throws SQLException
     */
    private Map<String, List<String>> getPKColumns(List<String> tableNames) throws SQLException {
        ITableOperator tableOperator = AppUtil.getBean(ITableOperator.class);
        return tableOperator.getPKColumns(tableNames);
    }

    /**
     * 获取主键字段
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    private List<String> getPKColumns(String tableName) throws SQLException {
        ITableOperator tableOperator = AppUtil.getBean(ITableOperator.class);
        return tableOperator.getPKColumns(tableName);
    }

    /**
     * 判断索引是否是主键索引。如果是，则将索引index的pkIndex属性设置为true。
     *
     * @param index
     * @return
     */
    private Index dedicatePKIndex(Index index) {
        try {
            List<String> pkCols = getPKColumns(index.getIndexName());
            if (isListEqual(index.getColumnList(), pkCols))
                index.setPkIndex(true);
            else
                index.setPkIndex(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    /**
     * 比较两个列表是否相等。在比较两个列表的元素时，比较的方式为(o==null ? e==null : o.equals(e)).
     *
     * @param list1
     * @param list2
     * @return
     */
    private boolean isListEqual(List<String> list1, List<String> list2) {
        if (list1 == null && list2 == null)// 2个都为null
            return true;
        if (list1 == null || list2 == null)// 2个有一个为null
            return false;
        if (list1.size() != list2.size())// 2个长度不相等
            return false;
        if (list1.containsAll(list2))
            return true;

        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dstz.base.db.table.BaseIndexOperator#rebuildIndex(java.lang.String, java.lang.String)
     */
    @Override
    public void rebuildIndex(String tableName, String indexName) {
        String sql = "SHOW CREATE TABLE " + tableName;
        List<String> ddls = jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("Create Table");
            }
        });
        String ddl = ddls.get(0);

        Pattern pattern = Pattern.compile("ENGINE\\s*=\\s*\\S+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ddl);
        if (matcher.find()) {
            String str = matcher.group();
            String sql_ = "ALTER TABLE " + tableName + " " + str;
            jdbcTemplate.execute(sql_);
            //System.out.println("sql_-------------->:"+ddl);
        }
    }

    /**
     * Mysql的schema
     *
     * @return
     */
    private String getSchema() {
        String schema = null;
        try {
            schema = jdbcTemplate.getDataSource().getConnection().getCatalog();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schema;
    }

    @Override
    public List<Index> getIndexByFuzzyMatch(String indexName) throws SQLException {
        List<String> allTableNames = getAllTableNames();
        List<Index> allIndexs = new ArrayList<Index>();
        for (String tableName : allTableNames) {
            allIndexs.addAll(getIndexsByTable(tableName));
        }

        List<Index> indexs = new ArrayList<Index>();
        for (Index index : allIndexs) {
            if (index.getIndexName().contains(indexName)) {
                indexs.add(index);
            }
        }
        return indexs;
    }

    @Override
    public List<Index> getIndexsByTable(String tableName) throws SQLException {
        String sql = new String("show index from ?tableName;");
        sql = sql.replace("?tableName", tableName);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<String> indexNames = new ArrayList<String>();
        List<Index> indexs = new ArrayList<Index>();
        for (Map<String, Object> row : rows) {
            String indexName = row.get("Key_name") + "";
            if (!indexNames.contains(indexName)) {
                indexNames.add(indexName);
                indexs.add(getIndex(tableName, indexName));
            }
        }

        return indexs;
    }

    @Override
    public Index getIndex(String tableName, String indexName) throws SQLException {
        String sql = new String("show index from ?tableName where key_name='?indexName';");
        sql = sql.replace("?tableName", tableName);
        sql = sql.replace("?indexName", indexName);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        Index index = null;
        List<String> columnList = new ArrayList<String>();
        for (Map<String, Object> row : rows) {
            /*
             * for(String key:row.keySet()){ System.out.println(key+":"+row.get(key)); }
             */
            if (index == null) {
                index = new DefaultIndex();
                index.setPkIndex(indexName.equals("PRIMARY"));
                index.setIndexComment(row.get("Comment") + "");
                index.setIndexName(row.get("Key_name") + "");
                index.setIndexType(row.get("Index_type") + "");
                index.setTableName(tableName);
                index.setUnique((row.get("Non_unique") + "").equals("0"));
            }
            columnList.add(row.get("Column_name") + "");
        }
        index.setColumnList(columnList);
        return index;
    }

    private List<String> getAllTableNames() {
        List<String> tableNames = new ArrayList<String>();

        String sql = "show tables;";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> row : rows) {
            for (Object object : row.values()) {
                tableNames.add(object + "");
            }
        }

        return tableNames;
    }
}
