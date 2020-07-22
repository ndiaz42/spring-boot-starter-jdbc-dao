package com.github.ndiaz.jdbc.autoconfigure;

import com.github.ndiaz.jdbc.dao.JdbcBaseDao;
import com.github.ndiaz.jdbc.util.SqlFileLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@ConditionalOnClass(JdbcBaseDao.class)
@EnableConfigurationProperties(JdbcDaoProperties.class)
public class JdbcDaoAutoConfiguration {

  private final JdbcDaoProperties properties;

  public JdbcDaoAutoConfiguration(final JdbcDaoProperties properties) {
    this.properties = properties;
  }

  @Bean
  @ConditionalOnProperty(prefix = "dao", name = "enable-sql-file")
  public SqlFileLoader sqlFileLoader(final Environment environment) {
    return new SqlFileLoader(this.properties.getPath(), environment);
  }

  @Bean
  @ConditionalOnMissingBean
  public JdbcBaseDao jdbcBaseDao(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    return new JdbcBaseDao(namedParameterJdbcTemplate);
  }

}
