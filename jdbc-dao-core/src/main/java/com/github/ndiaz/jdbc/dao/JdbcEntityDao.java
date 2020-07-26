package com.github.ndiaz.jdbc.dao;

import com.github.ndiaz.jdbc.exception.DaoException;
import com.github.ndiaz.jdbc.mapper.RowUnmapper;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

/**
 * A base JDBC DAO for a specific domain class.
 * Contains the basic methods required for fetching, inserting and updating domain objects.
 *
 * <p>It's recommended to implement a <a href="#{@link}">{@link RowMapper}</a> (annotated with
 * {@code @Component}) to fetch the objects. If not, a
 * <a href="#{@link}">{@link BeanPropertyRowMapper}</a> will be used.
 *
 * <p>It's recommended to implement a <a href="#{@link}">{@link RowUnmapper}</a> (annotated with
 * {@code @Component}) to update the objects. If not, a
 * <a href="#{@link}">{@link DefaultRowUnmapper}</a> will be used.
 *
 * @param <T> the domain class
 * @author ndiaz42
 * @see RowMapper
 * @see RowUnmapper
 */
@Slf4j
public abstract class JdbcEntityDao<T> {

  private final String className;
  @Autowired
  private JdbcBaseDao dao;
  @Autowired(required = false)
  private RowMapper<T> rowMapper;
  @Autowired(required = false)
  private RowUnmapper<T> rowUnmapper;

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
  }

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
   * Fetches a domain object.
   *
   * @param sql the sql query to execute
   * @return the domain object
   */
  protected T queryForObject(final String sql) {
    return dao.queryForObject(sql, rowMapper);
  }

  /**
   * Fetches a domain object with the given query parameters.
   *
   * @param sql    the sql query to execute
   * @param params the parameters of the query
   * @return the domain object
   */
  protected T queryForObject(final String sql, final AbstractSqlParameterSource params) {
    return dao.queryForObject(sql, params, rowMapper);
  }

  /**
   * Fetches a domain object with the given query parameters.
   *
   * @param sql    the sql query to execute
   * @param entity the domain object, using its attributes as query parameters
   * @return the domain object
   * @see RowUnmapper
   */
  protected T queryForObject(final String sql, final T entity) {
    return dao.queryForObject(sql, rowUnmapper.getSqlParameters(entity), rowMapper);
  }

  /**
   * Fetches a list of domain objects.
   *
   * @param sql the sql query to execute
   * @return the list of domain objects
   */
  protected List<T> queryForList(final String sql) {
    return dao.queryForList(sql, rowMapper);
  }

  /**
   * Fetches a list of domain objects with the given query parameters.
   *
   * @param sql    the sql query to execute
   * @param params the parameters of the query
   * @return the list of domain objects
   */
  protected List<T> queryForList(final String sql, final AbstractSqlParameterSource params) {
    return dao.queryForList(sql, params, rowMapper);
  }

  /**
   * Fetches a list of domain objects with the given query parameters.
   *
   * @param sql    the sql query to execute
   * @param entity the domain object, using its attributes as query parameters
   * @return the list of domain objects
   * @see RowUnmapper
   */
  protected List<T> queryForList(final String sql, final T entity) {
    return dao.queryForList(sql, rowUnmapper.getSqlParameters(entity), rowMapper);
  }

  /**
   * Fetches a page of domain objects.
   *
   * @param sql      the sql query to execute
   * @param pageable the pagination information
   * @return the page of domain objects
   * @see Page
   * @see Pageable
   */
  protected Page<T> queryForPage(final String sql, final Pageable pageable) {
    return dao.queryForPage(sql, pageable, rowMapper);
  }

  /**
   * Fetches a page of domain objects with the given query parameters.
   *
   * @param sql      the sql query to execute
   * @param params   the parameters of the query
   * @param pageable the pagination information
   * @return the page of domain objects
   * @see Page
   * @see Pageable
   */
  protected Page<T> queryForPage(final String sql, final AbstractSqlParameterSource params,
                                 final Pageable pageable) {
    return dao.queryForPage(sql, params, pageable, rowMapper);
  }

  /**
   * Fetches a page of domain objects with the given query parameters.
   *
   * @param sql      the sql query to execute
   * @param entity   the domain object, using its attributes as query parameters
   * @param pageable the pagination information
   * @return the page of domain objects
   * @see Page
   * @see Pageable
   * @see RowUnmapper
   */
  protected Page<T> queryForPage(final String sql, final T entity, final Pageable pageable) {
    return dao.queryForPage(sql, rowUnmapper.getSqlParameters(entity), pageable, rowMapper);
  }

  /**
   * Inserts a domain object.
   *
   * @param sql    the sql to execute
   * @param entity the domain object to insert
   * @return the number of affected rows
   * @see RowUnmapper
   */
  protected Integer insert(final String sql, final T entity) {
    return dao.insert(sql, rowUnmapper.getSqlParameters(entity));
  }

  /**
   * Inserts a domain object.
   *
   * @param sql            the sql to execute
   * @param entity         the domain object to insert
   * @param keyColumnNames the names of the columns that will have keys generated for them
   * @return the id of the inserted domain object
   * @throws DaoException if the domain object could not be inserted
   * @see RowUnmapper
   */
  protected Number insert(final String sql, final T entity, final List<String> keyColumnNames)
      throws DaoException {
    return dao.insert(sql, rowUnmapper.getSqlParameters(entity), keyColumnNames);
  }

  /**
   * Updates a domain object.
   *
   * @param sql the sql to execute
   * @return the number of affected rows
   */
  protected Integer update(final String sql) {
    return dao.update(sql);
  }

  /**
   * Updates a domain object with the given object.
   *
   * @param sql    the sql to execute
   * @param entity the domain object to update
   * @return the number of affected rows
   * @see RowUnmapper
   */
  protected Integer update(final String sql, final T entity) {
    return dao.update(sql, rowUnmapper.getSqlParameters(entity));
  }

  /**
   * Updates a domain object with the given query parameters.
   *
   * @param sql    the sql to execute
   * @param params the parameters of the query
   * @return the number of affected rows
   */
  protected Integer update(final String sql, final AbstractSqlParameterSource params) {
    return dao.update(sql, params);
  }

  /**
   * Batch update a list of domain objects.
   *
   * @param sql      the sql to execute in a batch
   * @param entities the list of domain objects to update
   * @return the list of number of affected rows per query (depends on database driver)
   * @see RowUnmapper
   */
  protected List<Integer> batchUpdate(final String sql, final List<T> entities) {
    return dao.batchUpdate(sql, rowUnmapper.getSqlParameters(entities));
  }

  /**
   * Batch update a list of domain objects with the given query parameters.
   *
   * @param sql    the sql to execute in a batch
   * @param params the list of query parameters
   * @return the list of number of affected rows per query (depends on database driver)
   * @see RowUnmapper
   */
  protected List<Integer> batchUpdate(final String sql, final AbstractSqlParameterSource[] params) {
    return dao.batchUpdate(sql, Arrays.asList(params));
  }

}
