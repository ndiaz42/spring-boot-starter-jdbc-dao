package com.github.ndiaz.jdbc.mapper.impl;

import com.github.ndiaz.jdbc.mapper.RowUnmapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

/**
 * A default implementation of <a href="#{@link}">{@link RowUnmapper}</a>, using
 * <a href="#{@link}">{@link BeanPropertySqlParameterSource}</a>.
 *
 * @param <T> the domain object's class
 */
public class DefaultRowUnmapper<T> implements RowUnmapper<T> {

  @Override
  public AbstractSqlParameterSource getSqlParameters(final T entity) {
    return new BeanPropertySqlParameterSource(entity);
  }

}
