package me.brandonli.allerexplore.endpoint;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.javalin.http.HandlerType;
import org.jetbrains.annotations.NotNull;

public final class DatabaseStorage implements Handler {

  private static final Path PATH = Paths.get("").resolve("allerexplore.db").toAbsolutePath();
  private static final String DB_URL = "jdbc:sqlite:%s".formatted(PATH);

  static {
    try (
            final Connection connection = DriverManager.getConnection(DB_URL);
            final PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS requests (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                            "ip_address TEXT, " +
                            "method TEXT, " +
                            "path TEXT, " +
                            "port INTEGER, " +
                            "user_agent TEXT, " +
                            "full_url TEXT)"
            )
    ) {
      statement.executeUpdate();
    } catch (final SQLException e) {
      throw new AssertionError(e);
    }
  }

  @Override
  public void handle(final @NotNull Context ctx) {
    final HandlerType method = ctx.method();
    final String methodType = method.toString();
    final String path = ctx.path();
    final String ip = ctx.ip();
    final int port = ctx.port();
    final String agent = ctx.userAgent();
    final String full = ctx.fullUrl();
    final String query = "INSERT INTO requests (timestamp, ip_address, method, path, port, user_agent, full_url) " +
            "VALUES (CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";
    try (
            final Connection connection = DriverManager.getConnection(DB_URL);
            final PreparedStatement statement = connection.prepareStatement(query)
    ) {
      statement.setString(1, ip);
      statement.setString(2, methodType);
      statement.setString(3, path);
      statement.setInt(4, port);
      statement.setString(5, agent);
      statement.setString(6, full);
      statement.executeUpdate();
    } catch (final SQLException ignored) {
    }
  }
}