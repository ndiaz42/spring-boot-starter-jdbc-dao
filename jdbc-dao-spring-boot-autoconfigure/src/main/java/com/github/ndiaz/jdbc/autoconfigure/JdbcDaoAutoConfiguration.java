package com.github.ndiaz.jdbc.autoconfigure;

import com.github.ndiaz.jdbc.dao.JdbcBaseDao;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@ConditionalOnClass(JdbcBaseDao.class)
public class JdbcDaoAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public JdbcBaseDao jdbcBaseDao(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    return new JdbcBaseDao(namedParameterJdbcTemplate);
  }

}
