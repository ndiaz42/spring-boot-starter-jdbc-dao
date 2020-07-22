package com.github.ndiaz.jdbc.util;

import com.github.ndiaz.jdbc.exception.DaoException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class SqlFileLoader {

  private static final Map<String, Map<String, String>> querys = new HashMap<>();
  private static final Map<String, String> tableNames = new HashMap<>();

  private final String path;
  private final Environment environment;

  public SqlFileLoader(final String path, final Environment environment) {
    this.path = path;
    this.environment = environment;
    init();
  }

  public String getSql(final String file, final String name) throws DaoException {
    if (querys.containsKey(file)) {
      final Map<String, String> entityQuerys = querys.get(file);
      if (entityQuerys.containsKey(name)) {
        return entityQuerys.get(name);
      }
    }
    log.error("Could not find sql query: {} - {}", file, name);
    throw new DaoException("Could not find sql query: " + file + " - " + name);
  }

  private void init() {
    try {
      final YamlPropertySourceLoader yamlLoader = new YamlPropertySourceLoader();
      List<PropertySource<?>> files =
          yamlLoader.load("tables.yml", new ClassPathResource(path + "tables.yml"));
      files.forEach(p -> log.info(p.getName() + "-" + p.getProperty(p.getName())));
    } catch (final IOException ex) {
      log.error(ex.getMessage(), ex);
      throw new BeanInitializationException(ex.getMessage(), ex);
    }

  }

}
