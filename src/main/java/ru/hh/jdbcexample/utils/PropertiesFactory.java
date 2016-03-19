package ru.hh.jdbcexample.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFactory {

  public static Properties load() throws IOException {
    final ClassLoader classLoader = PropertiesFactory.class.getClassLoader();

    final Properties defaultProperties = new Properties();
    try (final InputStream defaultInputStream = classLoader.getResourceAsStream("default.properties")) {
      defaultProperties.load(defaultInputStream);
    }

    try (final InputStream overriddenInputStream = classLoader.getResourceAsStream("overridden.properties")) {
      if (overriddenInputStream == null) {
        return defaultProperties;
      }
      final Properties overriddenProperties = new Properties(defaultProperties);
      overriddenProperties.load(overriddenInputStream);
      return overriddenProperties;
    }
  }

  private PropertiesFactory() {
  }
}
