package com.github.ndiaz.jdbc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collector;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public interface PaginationHelper {

  static <D> Page<D> fetchPage(final PageFetcher<D> pageFetcher) {
    final NamedParameterJdbcTemplate jdbcTemplate = pageFetcher.getJdbcTemplate();
    final String query = pageFetcher.getQuery();
    final CombinedSqlParameterSource parameters =
        new CombinedSqlParameterSource(pageFetcher.getParameters());
    final RowMapper<D> rowMapper = pageFetcher.getRowMapper();
    final Pageable pageable = pageFetcher.getPageable();

    final String sqlCount = " select count(*) from ( " + pageFetcher.getQuery() + " ) as X ";
    final Long rowCount = jdbcTemplate.queryForObject(sqlCount, parameters, Long.class);

    final int pageCount = (int) Math.ceil((float) rowCount / pageable.getPageSize());
    final int startRow = pageable.getPageNumber() * pageable.getPageSize() + 1;
    final int endRow = pageable.getPageNumber() * pageable.getPageSize() + pageable.getPageSize();
    parameters.addValue("startRow", startRow);
    parameters.addValue("endRow", endRow);

    final StringBuilder sql = new StringBuilder();
    sql.append(" select * from ( ");
    sql.append(" select row_number() over() as ROWID, A.* from ( ");
    sql.append(query);
    sql.append(" ) as A ) as B ");
    sql.append(" where B.ROWID between :startRow and :endRow ");
    final String orderBy = pageable.getSort().stream()
        .map(o -> o.getProperty() + StringUtils.SPACE + o.getDirection().name()).collect(Collector
            .of(() -> new StringJoiner(", ", " order by ", StringUtils.EMPTY)
                    .setEmptyValue(StringUtils.EMPTY), StringJoiner::add, StringJoiner::merge,
                StringJoiner::toString));
    sql.append(orderBy);

    List<D> content = new ArrayList<>();
    if (rowMapper != null) {
      content = jdbcTemplate.query(sql.toString(), parameters, rowMapper);
    }

    return new PageImpl<>(content,
        PageRequest.of(Math.min(pageable.getPageNumber(), pageCount), pageable.getPageSize()),
        rowCount);
  }

}
