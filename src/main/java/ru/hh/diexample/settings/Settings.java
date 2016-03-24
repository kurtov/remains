package ru.hh.diexample.settings;

import com.typesafe.config.Config;

import static java.util.Objects.requireNonNull;

public class Settings {

  private final Config config;

  public Settings(final Config config) {
    this.config = requireNonNull(config);
  }

  public String databaseUrl() {
    return config.getString("database.url");
  }

  public String databaseUser() {
    return config.getString("database.user");
  }

  public String databasePassword() {
    return config.getString("database.password");
  }
}
