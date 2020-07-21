package com.github.ndiaz.jdbc.mapper.impl;

import java.sql.ResultSet;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DefaultResultSetExtractor<T> implements ResultSetExtractor<T> {

  @Override
  public T extractData(final ResultSet rs) {
    return null;
  }

}
