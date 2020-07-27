package com.github.ndiaz.jdbc.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

/**
 * A {@code RowUnmapper} transforms a domain object into a map, used by the library when executing
 * create or update queries.
 *
 * @param <T> the domain object's class
 * @see AbstractSqlParameterSource
 */
public interface RowUnmapper<T> {

  /**
   * Gets the sql parameters as a map.
   *
   * @param entity the domain object to map
   * @return the sql parameters
   */
  AbstractSqlParameterSource getSqlParameters(final T entity);

  /**
   * Gets the sql parameters as a list of maps. Used on batch updates.
   *
   * @param entities the domain objects to map
   * @return the list of sql parameters
   */
  default List<AbstractSqlParameterSource> getSqlParameters(final List<T> entities) {
    final List<AbstractSqlParameterSource> params = new ArrayList<>();
    for (final T entity : entities) {
      params.add(getSqlParameters(entity));
    }
    return params;
  }

}
