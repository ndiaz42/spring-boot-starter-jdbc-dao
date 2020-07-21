package com.github.ndiaz.jdbc.util;

import java.util.Map;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class CombinedSqlParameterSource extends AbstractSqlParameterSource {

  private final MapSqlParameterSource mapSqlParameterSource;
  private final AbstractSqlParameterSource abstractSqlParameterSource;

  public CombinedSqlParameterSource(final Object object) {
    mapSqlParameterSource = new MapSqlParameterSource();
    abstractSqlParameterSource = new BeanPropertySqlParameterSource(object);
  }

  public CombinedSqlParameterSource(final AbstractSqlParameterSource abstractSqlParameterSource) {
    mapSqlParameterSource = new MapSqlParameterSource();
    this.abstractSqlParameterSource = abstractSqlParameterSource;
  }

  public CombinedSqlParameterSource addValue(final String paramName, final Object value) {
    mapSqlParameterSource.addValue(paramName, value);
    return this;
  }

  public CombinedSqlParameterSource addValue(final String paramName, final Object value,
                                             final int sqlType) {
    mapSqlParameterSource.addValue(paramName, value, sqlType);
    return this;
  }

  public CombinedSqlParameterSource addValues(final Map<String, ?> values) {
    mapSqlParameterSource.addValues(values);
    return this;
  }

  @Override
  public boolean hasValue(final String paramName) {
    return abstractSqlParameterSource.hasValue(paramName)
        || mapSqlParameterSource.hasValue(paramName);
  }

  @Override
  public Object getValue(final String paramName) {
    return abstractSqlParameterSource.hasValue(paramName)
        ? abstractSqlParameterSource.getValue(paramName)
        : mapSqlParameterSource.getValue(paramName);
  }

  @Override
  public int getSqlType(final String paramName) {
    return abstractSqlParameterSource.hasValue(paramName)
        ? abstractSqlParameterSource.getSqlType(paramName)
        : mapSqlParameterSource.getSqlType(paramName);
  }

}

