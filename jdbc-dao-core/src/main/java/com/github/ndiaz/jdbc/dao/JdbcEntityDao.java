package com.github.ndiaz.jdbc.dao;

import com.github.ndiaz.jdbc.exception.DaoException;
import com.github.ndiaz.jdbc.mapper.RowUnmapper;
import com.github.ndiaz.jdbc.mapper.impl.DefaultResultSetExtractor;
import com.github.ndiaz.jdbc.mapper.impl.DefaultRowUnmapper;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

@Slf4j
public abstract class JdbcEntityDao<T> {

  private final String className;
  @Autowired
  private JdbcBaseDao dao;
  @Autowired(required = false)
  private RowMapper<T> rowMapper;
  @Autowired(required = false)
  private RowUnmapper<T> rowUnmapper;
  @Autowired(required = false)
  private ResultSetExtractor<T> resultSetExtractor;

  public JdbcEntityDao() {
    className = getClassTSimpleName();
  }

  @SuppressWarnings("unchecked")
  private Class<T> getClassT() {
    return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
        .getActualTypeArguments()[0];
  }

  private String getClassTSimpleName() {
    return getClassT().getSimpleName();
  }

  @PostConstruct
  private void checkMappers() {
    if (rowMapper == null) {
      log.info("No implementation of RowMapper<{}> found. Using DefaultRowMapper instead.",
          className);
      rowMapper = new BeanPropertyRowMapper<>(getClassT());
    }
    if (rowUnmapper == null) {
      log.info("No implementation of RowUnmapper<{}> found. Using DefaultRowUnmapper instead.",
          className);
      rowUnmapper = new DefaultRowUnmapper<>();
    }
    if (resultSetExtractor == null) {
      log.info(
          "No implementation of ResultSetExtractor<{}> found. Using DefaultResultSetExtractor instead.",
          className);
      resultSetExtractor = new DefaultResultSetExtractor<>();
    }
  }

  protected T query(final String sql) {
    return dao.query(sql, resultSetExtractor);
  }

  protected T query(final String sql, final AbstractSqlParameterSource params) {
    return dao.query(sql, params, resultSetExtractor);
  }

  protected T query(final String sql, final T entity) {
    return dao.query(sql, rowUnmapper.getSqlParameters(entity), resultSetExtractor);
  }

  protected T queryForObject(final String sql) {
    return dao.queryForObject(sql, rowMapper);
  }

  protected T queryForObject(final String sql, final AbstractSqlParameterSource params) {
    return dao.queryForObject(sql, params, rowMapper);
  }

  protected T queryForObject(final String sql, final T entity) {
    return dao.queryForObject(sql, rowUnmapper.getSqlParameters(entity), rowMapper);
  }

  protected List<T> queryForList(final String sql) {
    return dao.queryForList(sql, rowMapper);
  }

  protected List<T> queryForList(final String sql, final AbstractSqlParameterSource params) {
    return dao.queryForList(sql, params, rowMapper);
  }

  protected List<T> queryForList(final String sql, final T entity) {
    return dao.queryForList(sql, rowUnmapper.getSqlParameters(entity), rowMapper);
  }

  protected Page<T> queryForPage(final String sql, final Pageable pageable) {
    return dao.queryForPage(sql, pageable, rowMapper);
  }

  protected Page<T> queryForPage(final String sql, final AbstractSqlParameterSource params,
                                 final Pageable pageable) {
    return dao.queryForPage(sql, params, pageable, rowMapper);
  }

  protected Page<T> queryForPage(final String sql, final T entity, final Pageable pageable) {
    return dao.queryForPage(sql, rowUnmapper.getSqlParameters(entity), pageable, rowMapper);
  }

  protected Integer insert(final String sql, final T entity) {
    return dao.insert(sql, rowUnmapper.getSqlParameters(entity));
  }

  protected Number insert(final String sql, final T entity, final List<String> keyColumnNames)
      throws DaoException {
    return dao.insert(sql, rowUnmapper.getSqlParameters(entity), keyColumnNames);
  }

  protected Integer update(final String sql) {
    return dao.update(sql);
  }

  protected Integer update(final String sql, final T entity) {
    return dao.update(sql, rowUnmapper.getSqlParameters(entity));
  }

  protected Integer update(final String sql, final AbstractSqlParameterSource params) {
    return dao.update(sql, params);
  }

  protected List<Integer> batchUpdate(final String sql, final List<T> entities) {
    return dao.batchUpdate(sql, rowUnmapper.getSqlParameters(entities));
  }

  protected List<Integer> batchUpdate(final String sql, final AbstractSqlParameterSource[] params) {
    return dao.batchUpdate(sql, Arrays.asList(params));
  }

}
