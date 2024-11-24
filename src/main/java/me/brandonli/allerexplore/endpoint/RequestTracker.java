package me.brandonli.allerexplore.endpoint;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import org.jetbrains.annotations.NotNull;

public final class RequestTracker implements Handler {

  @Override
  public void handle(final @NotNull Context ctx) {
    final HandlerType method = ctx.method();
    final String path = ctx.path();
    final String ip = ctx.ip();
    final int port = ctx.port();
    final String agent = ctx.userAgent();
    final String full = ctx.fullUrl();
    System.out.println("Received request: " + method + " " + path + " from " + ip + ":" + port + " with " + agent + " at " + full);
  }
}
