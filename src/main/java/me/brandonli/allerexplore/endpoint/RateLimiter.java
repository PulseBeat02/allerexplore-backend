package me.brandonli.allerexplore.endpoint;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.util.NaiveRateLimit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class RateLimiter implements Handler {

  @Override
  public void handle(@NotNull final Context ctx) {
    NaiveRateLimit.requestPerTimeUnit(ctx, 10, TimeUnit.MINUTES);
  }
}