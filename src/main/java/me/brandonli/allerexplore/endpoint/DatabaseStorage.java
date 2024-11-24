/*

MIT License

Copyright (c) 2024 Brandon Li

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
package me.brandonli.allerexplore.endpoint;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    final String query =
      "INSERT INTO requests (timestamp, ip_address, method, path, port, user_agent, full_url) " +
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
    } catch (final SQLException ignored) {}
  }
}
