package com.gihub.ndiaz.jdbc.dao;

import com.gihub.ndiaz.jdbc.exception.DaoException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class JdbcBaseDao {

  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;

  <D> D query(final String sql, final ResultSetExtractor<D> rse) {
    return query(sql, new MapSqlParameterSource(), rse);
  }

  <D> D query(final String sql, final AbstractSqlParameterSource params,
              final ResultSetExtractor<D> rse) {
    return jdbcTemplate.query(sql, params, rse);
  }

  <D> D queryForObject(final String sql, final RowMapper<D> rowMapper) {
    return queryForObject(sql, new MapSqlParameterSource(), rowMapper);
  }

  <D> D queryForObject(final String sql, final AbstractSqlParameterSource params,
                       final RowMapper<D> rowMapper) {
    return jdbcTemplate.queryForObject(sql, params, rowMapper);
  }

  <D> List<D> queryForList(final String sql, final RowMapper<D> rowMapper) {
    return queryForList(sql, new MapSqlParameterSource(), rowMapper);
  }

  <D> List<D> queryForList(final String sql, final AbstractSqlParameterSource params,
                           final RowMapper<D> rowMapper) {
    return jdbcTemplate.query(sql, params, rowMapper);
  }

  <D> Page<D> queryForPage(final String sql, final Pageable pageable,
                           final RowMapper<D> rowMapper) {
    return queryForPage(sql, new MapSqlParameterSource(), pageable, rowMapper);
  }

  <D> Page<D> queryForPage(final String sql, final AbstractSqlParameterSource params,
                           final Pageable pageable, final RowMapper<D> rowMapper) {
    // TODO
    return null;
  }

  Integer insert(final String sql, final AbstractSqlParameterSource params) {
    return update(sql, params);
  }

  Number insert(final String sql, final AbstractSqlParameterSource params,
                final List<String> keyColumnNames) throws DaoException {
    final KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
    final int affectedRows = jdbcTemplate
        .update(sql, params, generatedKeyHolder, keyColumnNames.toArray(new String[0]));
    if (affectedRows <= 0) {
      throw new DaoException("Could not insert row into database");
    }
    return generatedKeyHolder.getKey();
  }

  Integer update(final String sql) {
    return update(sql, new MapSqlParameterSource());
  }

  Integer update(final String sql, final AbstractSqlParameterSource params) {
    return jdbcTemplate.update(sql, params);
  }

  List<Integer> batchUpdate(final String sql, final List<AbstractSqlParameterSource> params) {
    final int[] results =
        jdbcTemplate.batchUpdate(sql, params.toArray(new AbstractSqlParameterSource[0]));
    return Arrays.stream(results).boxed().collect(Collectors.toList());
  }

}
