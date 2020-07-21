package com.github.ndiaz.jdbc.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Builder
@Getter
public class PageFetcher<T> {

  @NonNull
  private final NamedParameterJdbcTemplate jdbcTemplate;
  @NonNull
  private final String query;
  @Builder.Default
  private final Pageable pageable = PageRequest.of(0, 20);
  @Builder.Default
  private final AbstractSqlParameterSource parameters = new MapSqlParameterSource();
  private final RowMapper<T> rowMapper;

  public static <T> PageFetcherBuilder<T> builder(final NamedParameterJdbcTemplate jdbcTemplate,
                                                  final String query,
                                                  final RowMapper<T> rowMapper) {
    final PageFetcherBuilder<T> builder = new PageFetcherBuilder<T>();
    builder.jdbcTemplate(jdbcTemplate);
    builder.query(StringUtils.trimToNull(query));
    builder.rowMapper(rowMapper);
    return builder;
  }

}
