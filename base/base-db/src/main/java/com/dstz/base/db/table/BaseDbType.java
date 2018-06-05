package com.dstz.base.db.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dstz.base.db.api.table.IDbType;

public class BaseDbType implements IDbType {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    // JdbcTemplate
    protected JdbcTemplate jdbcTemplate;


    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
