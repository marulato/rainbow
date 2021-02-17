package org.avalon.rainbow.common.jdbc;

import org.avalon.rainbow.common.base.BasePO;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseQueryTemplate {

    private final JdbcTemplate jdbcTemplate = BeanUtils.getBean(JdbcTemplate.class);

    protected <T> List<T> query(QueryBuilder<T> queryBuilder) {
        return jdbcTemplate.query(queryBuilder.getSql(), queryBuilder.getRowMapper(), queryBuilder.getParameters());
    }

    protected <T> T queryForObject(QueryBuilder<T> queryBuilder) {
        try {
            return jdbcTemplate.queryForObject(queryBuilder.getSql(), queryBuilder.getRowMapper(), queryBuilder.getParameters());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    protected void mapAuditColumns(BasePO po, ResultSet rs) throws SQLException {
        po.setCreatedAt(rs.getTimestamp("CREATED_AT"));
        po.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
        po.setCreatedBy(rs.getString("CREATED_BY"));
        po.setUpdatedBy(rs.getString("UPDATED_BY"));
        po.setCreatedDm(rs.getString("CREATED_DM"));
        po.setUpdatedDm(rs.getString("UPDATED_DM"));
    }
}
