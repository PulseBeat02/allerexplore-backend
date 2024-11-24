package me.brandonli.allerexplore.endpoint;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DatabaseStorage implements Handler {

  private static final Path PATH = Paths.get("").resolve("allerexplore.db").toAbsolutePath();
  private static final String DB_URL = "jdbc:sqlite:%s".formatted(PATH);

  static {
    try (final Connection connection = DriverManager.getConnection(DB_URL);
         final PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS requests (id INTEGER PRIMARY KEY AUTOINCREMENT, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, ip_address TEXT)")) {
      statement.executeUpdate();
    } catch (final SQLException e) {
      throw new AssertionError(e);
    }
  }

  @Override
  public void handle(final @NotNull Context ctx) throws Exception {
    final String ipAddress = ctx.ip();
    final String query = "INSERT INTO requests (timestamp, ip_address) VALUES (CURRENT_TIMESTAMP, ?)";
    try (final Connection connection = DriverManager.getConnection(DB_URL);
         final PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, ipAddress);
      statement.executeUpdate();
    } catch (final SQLException ignored) {
    }
  }
}