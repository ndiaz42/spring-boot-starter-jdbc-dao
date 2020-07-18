package com.gihub.ndiaz.jdbc.mapper.impl;

import com.gihub.ndiaz.jdbc.mapper.RowUnmapper;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Component;

@Component
@Order
public class DefaultRowUnmapper<T> implements RowUnmapper<T> {

  @Override
  public AbstractSqlParameterSource getSqlParameters(final T entity) {
    return new BeanPropertySqlParameterSource(entity);
  }

}
