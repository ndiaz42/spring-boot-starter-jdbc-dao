package com.gihub.ndiaz.jdbc.mapper;

import java.sql.ResultSet;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
@Order
public class DefaultResultSetExtractor<T> implements ResultSetExtractor<T> {

  @Override
  public T extractData(final ResultSet rs) {
    return null;
  }

}

