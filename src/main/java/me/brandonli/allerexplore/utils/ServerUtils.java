package me.brandonli.allerexplore.utils;

import io.javalin.config.JavalinConfig;

import java.util.function.Consumer;

public final class ServerUtils {

  private ServerUtils() {
  }

  public static Consumer<JavalinConfig> createConfig() {
    return (final JavalinConfig javalinConfig) -> {
      javalinConfig.useVirtualThreads = true;
    };
  }
}
