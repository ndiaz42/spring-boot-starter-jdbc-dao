package com.github.ndiaz.jdbc.mapper.impl;

import com.github.ndiaz.jdbc.mapper.RowUnmapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

public class DefaultRowUnmapper<T> implements RowUnmapper<T> {

  @Override
  public AbstractSqlParameterSource getSqlParameters(final T entity) {
    return new BeanPropertySqlParameterSource(entity);
  }

}
