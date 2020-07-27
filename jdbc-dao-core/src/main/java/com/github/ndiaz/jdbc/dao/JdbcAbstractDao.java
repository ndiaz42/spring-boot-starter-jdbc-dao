package com.github.ndiaz.jdbc.dao;

import com.github.ndiaz.jdbc.exception.DaoException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

/**
 * A base JDBC DAO for a generic DAO implementation.
 * Contains the basic methods required for fetching, inserting and updating.
 *
 * @author ndiaz42
 */
public abstract class JdbcAbstractDao {

  @Autowired
  private JdbcBaseDao dao;

  /**
   * Gets a sql query string from a YAML file.
   *
   * @param file the name of the sql file, without extension
   * @param name the name of the query (located inside the file)
   * @return the sql query
   * @throws DaoException if the query could not be found
   */
  protected String getSql(final String file, final String name) throws DaoException {
    return dao.getSql(file, name);
  }

  /**
   * Fetches an object mapping the result row via a <a href="#{@link}">{@link RowMapper}</a>.
   *
   * @param sql       the sql query to execute
   * @param rowMapper the object that will map one object per row
   * @return the mapped object
   * @see RowMapper
   */
  protected <D> D queryForObject(final String sql, final RowMapper<D> rowMapper) {
    return dao.queryForObject(sql, rowMapper);
  }

  /**
   * Fetches an object with the given query parameters, mapping the result row via a
   * <a href="#{@link}">{@link RowMapper}</a>.
   *
   * @param sql       the sql query to execute
   * @param params    the parameters of the query
   * @param rowMapper the object that will map one object per row
   * @return the mapped object
   * @see RowMapper
   */
  protected <D> D queryForObject(final String sql, final AbstractSqlParameterSource params,
                                 final RowMapper<D> rowMapper) {
    return dao.queryForObject(sql, params, rowMapper);
  }

  /**
   * Fetches a list of objects, mapping the each row via a <a href="#{@link}">{@link RowMapper}</a>.
   *
   * @param sql       the sql query to execute
   * @param rowMapper the object that will map one object per row
   * @return the list of mapped objects
   * @see RowMapper
   */
  protected <D> List<D> queryForList(final String sql, final RowMapper<D> rowMapper) {
    return dao.queryForList(sql, rowMapper);
  }

  /**
   * Fetches a list of objects with the given query parameters, mapping the each row via a
   * <a href="#{@link}">{@link RowMapper}</a>.
   *
   * @param sql       the sql query to execute
   * @param params    the parameters of the query
   * @param rowMapper the object that will map one object per row
   * @return the list of mapped objects
   * @see RowMapper
   */
  protected <D> List<D> queryForList(final String sql, final AbstractSqlParameterSource params,
                                     final RowMapper<D> rowMapper) {
    return dao.queryForList(sql, params, rowMapper);
  }

  /**
   * Fetches a page of objects mapping the each row via a <a href="#{@link}">{@link RowMapper}</a>.
   *
   * @param sql       the sql query to execute
   * @param pageable  the pagination information
   * @param rowMapper the object that will map one object per row
   * @return the page of mapped objects
   * @see Page
   * @see Pageable
   * @see RowMapper
   */
  protected <D> Page<D> queryForPage(final String sql, final Pageable pageable,
                                     final RowMapper<D> rowMapper) {
    return dao.queryForPage(sql, pageable, rowMapper);
  }

  /**
   * Fetches a page of objects with the given query parameters, mapping the each row via a
   * <a href="#{@link}">{@link RowMapper}</a>.
   *
   * @param sql       the sql query to execute
   * @param params    the parameters of the query
   * @param pageable  the pagination information
   * @param rowMapper the object that will map one object per row
   * @return the page of mapped objects
   * @see Page
   * @see Pageable
   * @see RowMapper
   */
  protected <D> Page<D> queryForPage(final String sql, final AbstractSqlParameterSource params,
                                     final Pageable pageable, final RowMapper<D> rowMapper) {
    return dao.queryForPage(sql, params, pageable, rowMapper);
  }

  /**
   * Performs a simple insert with the given query parameters.
   *
   * @param sql    the sql to execute
   * @param params the parameters of the query
   * @return the number of affected rows
   */
  protected Integer insert(final String sql, final AbstractSqlParameterSource params) {
    return dao.insert(sql, params);
  }

  /**
   * Performs a simple insert with the given query parameters.
   *
   * @param sql            the sql to execute
   * @param params         the parameters of the query
   * @param keyColumnNames the names of the columns that will have keys generated for them
   * @return the id of the inserted row
   * @throws DaoException if the operation failed
   */
  protected Number insert(final String sql, final AbstractSqlParameterSource params,
                          final List<String> keyColumnNames) throws DaoException {
    return dao.insert(sql, params, keyColumnNames);
  }

  /**
   * Performs a simple update.
   *
   * @param sql the sql to execute
   * @return the number of affected rows
   */
  protected Integer update(final String sql) {
    return dao.update(sql);
  }

  /**
   * Performs a simple update with the given query parameters.
   *
   * @param sql    the sql to execute
   * @param params the parameters of the query
   * @return the number of affected rows
   */
  protected Integer update(final String sql, final AbstractSqlParameterSource params) {
    return dao.update(sql, params);
  }

  /**
   * Performs a simple batch update with the given query parameters.
   *
   * @param sql    the sql to execute
   * @param params the list of query parameters
   * @return the list of number of affected rows per query (depends on database driver)
   */
  protected List<Integer> batchUpdate(final String sql, final AbstractSqlParameterSource[] params) {
    return dao.batchUpdate(sql, Arrays.asList(params));
  }

}
