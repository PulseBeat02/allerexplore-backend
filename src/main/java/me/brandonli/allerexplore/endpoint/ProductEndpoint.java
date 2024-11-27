package me.brandonli.allerexplore.endpoint;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import me.brandonli.allerexplore.barcode.Barcode;
import org.jetbrains.annotations.NotNull;
import pl.coderion.model.Product;

public final class ProductEndpoint implements Handler {

  @Override
  public void handle(@NotNull final Context ctx) {

    final String id = ctx.queryParam("id");
    if (id == null) {
      ctx.status(HttpStatus.NOT_FOUND);
      return;
    }

    final Barcode barcode = Barcode.of("EAN_13", id);
    final Product product = barcode.getProduct();
    if (product == null) {
      ctx.status(HttpStatus.NOT_FOUND);
      return;
    }

    ctx.json(product);
  }
}
