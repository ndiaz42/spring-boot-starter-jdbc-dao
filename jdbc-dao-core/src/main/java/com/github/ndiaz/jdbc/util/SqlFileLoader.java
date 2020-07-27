package com.github.ndiaz.jdbc.util;

import com.github.ndiaz.jdbc.exception.DaoException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

/**
 * {@code SqlFileLoader} searches YAML files in src/main/resources/{@code path} and loads the
 * queries in memory at launch for later use.
 */
@Slf4j
public class SqlFileLoader {

  private static final Map<String, Map<String, String>> queries = new HashMap<>();

  private final String path;
  private final ResourcePatternResolver resourceResolver;

  public SqlFileLoader(final String path, final ResourcePatternResolver resourceResolver) {
    this.path = path;
    this.resourceResolver = resourceResolver;
  }

  /**
   * Gets a sql query string from a YAML file.
   *
   * @param file the name of the sql file, without extension
   * @param name the name of the query (located inside the file)
   * @return the sql query
   * @throws DaoException if the query could not be found
   */
  public String getSql(final String file, final String name) throws DaoException {
    log.trace("Looking for SQL file '{}.yml' and query name '{}'...", file, name);
    if (queries.containsKey(file)) {
      final Map<String, String> queriesFile = queries.get(file);
      if (queriesFile.containsKey(name)) {
        return queriesFile.get(name);
      }
    }
    log.error("Could not find sql query: '{}.yml' - '{}'", file, name);
    throw new DaoException("Could not find sql query: '" + file + ".yml' - '" + name + "'");
  }

  @PostConstruct
  private void init() {
    try {
      final YamlMapFactoryBean yamlMapFactoryBean = new YamlMapFactoryBean();
      final Resource[] queryResources =
          resourceResolver.getResources(path + "/**/*.yml");
      for (final Resource queryResource : queryResources) {
        yamlMapFactoryBean.setResources(queryResource);
        final Map<String, Object> map = yamlMapFactoryBean.getObject();
        if (map != null && queryResource.getFilename() != null) {
          final Map<String, String> queryMap = map.entrySet().stream()
              .collect(Collectors.toMap(Map.Entry::getKey, e -> (String) e.getValue()));
          queries.put(StringUtils.stripFilenameExtension(queryResource.getFilename()), queryMap);
          log.info("Loaded sql queries from '{}'", queryResource.getFilename());
        }
      }
    } catch (final IOException ex) {
      log.error(ex.getMessage(), ex);
      throw new BeanInitializationException(ex.getMessage(), ex);
    }
  }

}
