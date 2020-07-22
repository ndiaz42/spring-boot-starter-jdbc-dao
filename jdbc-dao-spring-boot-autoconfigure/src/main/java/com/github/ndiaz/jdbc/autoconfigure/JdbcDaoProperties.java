package com.github.ndiaz.jdbc.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(prefix = JdbcDaoProperties.JDBC_DAO_PREFIX)
public class JdbcDaoProperties {

  public static final String JDBC_DAO_PREFIX = "dao";

  private Boolean enableSqlFile = false;

  private String path = "db/";

}
