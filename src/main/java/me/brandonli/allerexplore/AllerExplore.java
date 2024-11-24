package me.brandonli.allerexplore;

import io.javalin.Javalin;
import me.brandonli.allerexplore.endpoint.BarcodeEndpoint;
import me.brandonli.allerexplore.endpoint.DatabaseStorage;
import me.brandonli.allerexplore.endpoint.RateLimiter;
import me.brandonli.allerexplore.utils.ServerUtils;

public final class AllerExplore {

  public static void main(final String[] args) {
    Javalin.create(ServerUtils.createConfig())
            .before(new DatabaseStorage())
            .before(new RateLimiter())
            .get("/barcode", new BarcodeEndpoint())
            .start(8080);
  }
}
