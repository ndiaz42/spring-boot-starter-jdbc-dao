package com.gihub.ndiaz.jdbc.dao;

import com.gihub.ndiaz.jdbc.exception.DaoException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

public abstract class JdbcAbstractDao {

  @Autowired
  private JdbcBaseDao dao;

  protected <D> D query(final String sql, final ResultSetExtractor<D> rse) {
    return dao.query(sql, rse);
  }

  protected <D> D query(final String sql, final AbstractSqlParameterSource params,
                        final ResultSetExtractor<D> rse) {
    return dao.query(sql, params, rse);
  }

  protected <D> D queryForObject(final String sql, final RowMapper<D> rowMapper) {
    return dao.queryForObject(sql, rowMapper);
  }

  protected <D> D queryForObject(final String sql, final AbstractSqlParameterSource params,
                                 final RowMapper<D> rowMapper) {
    return dao.queryForObject(sql, params, rowMapper);
  }

  protected <D> List<D> queryForList(final String sql, final RowMapper<D> rowMapper) {
    return dao.queryForList(sql, rowMapper);
  }

  protected <D> List<D> queryForList(final String sql, final AbstractSqlParameterSource params,
                                     final RowMapper<D> rowMapper) {
    return dao.queryForList(sql, params, rowMapper);
  }

  protected Integer insert(final String sql, final AbstractSqlParameterSource params) {
    return dao.insert(sql, params);
  }

  protected Number insert(final String sql, final AbstractSqlParameterSource params,
                          final List<String> keyColumnNames) throws DaoException {
    return dao.insert(sql, params, keyColumnNames);
  }

  protected Integer update(final String sql) {
    return dao.update(sql);
  }

  protected Integer update(final String sql, final AbstractSqlParameterSource params) {
    return dao.update(sql, params);
  }

  protected List<Integer> batchUpdate(final String sql, final AbstractSqlParameterSource[] params) {
    return dao.batchUpdate(sql, Arrays.asList(params));
  }

}
