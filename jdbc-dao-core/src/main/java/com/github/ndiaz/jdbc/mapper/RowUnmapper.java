package com.github.ndiaz.jdbc.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

public interface RowUnmapper<T> {

  AbstractSqlParameterSource getSqlParameters(final T entity);

  default List<AbstractSqlParameterSource> getSqlParameters(final List<T> entities) {
    final List<AbstractSqlParameterSource> params = new ArrayList<>();
    for (final T entity : entities) {
      params.add(getSqlParameters(entity));
    }
    return params;
  }

}
