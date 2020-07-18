package com.gihub.ndiaz.jdbc.dao;

import com.gihub.ndiaz.jdbc.exception.DaoException;
import com.gihub.ndiaz.jdbc.mapper.impl.DefaultResultSetExtractor;
import com.gihub.ndiaz.jdbc.mapper.impl.DefaultRowMapper;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

@Slf4j
public abstract class JdbcEntityDao<T> {

  private final String className;
  @Autowired
  private JdbcBaseDao dao;
  @Autowired
  private RowMapper<T> rowMapper;
  @Autowired
  private ResultSetExtractor<T> resultSetExtractor;

  public JdbcEntityDao() {
    className = getClassSimpleName();
  }

  @SuppressWarnings("unchecked")
  private String getClassSimpleName() {
    return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
        .getActualTypeArguments()[0]).getSimpleName();
  }

  @PostConstruct
  private void checkMappers() {
    if (rowMapper instanceof DefaultRowMapper<?>) {
      log.info("No implementation of RowMapper<{}> found. Using DefaultRowMapper instead.",
          className);
    }
    if (resultSetExtractor instanceof DefaultResultSetExtractor<?>) {
      log.info(
          "No implementation of ResultSetExtractor<{}> found. "
              + "Using DefaultResultSetExtractor instead.", className);
    }
  }

  protected T query(final String sql) {
    return dao.query(sql, resultSetExtractor);
  }

  protected T query(final String sql, final AbstractSqlParameterSource params) {
    return dao.query(sql, params, resultSetExtractor);
  }

  protected T queryForObject(final String sql) {
    return dao.queryForObject(sql, rowMapper);
  }

  protected T queryForObject(final String sql, final AbstractSqlParameterSource params) {
    return dao.queryForObject(sql, params, rowMapper);
  }

  protected List<T> queryForList(final String sql) {
    return dao.queryForList(sql, rowMapper);
  }

  protected List<T> queryForList(final String sql, final AbstractSqlParameterSource params) {
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
