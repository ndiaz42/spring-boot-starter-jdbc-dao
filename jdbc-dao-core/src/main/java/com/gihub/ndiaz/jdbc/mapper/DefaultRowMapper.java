package com.gihub.ndiaz.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@Order
public class DefaultRowMapper<T> implements RowMapper<T> {

  private final BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<>();

  @Override
  public T mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return rowMapper.mapRow(rs, rowNum);
  }

}

