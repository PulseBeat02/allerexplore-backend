package me.brandonli.allerexplore.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import me.brandonli.allerexplore.barcode.Barcode;
import me.brandonli.allerexplore.barcode.BarcodeDecoder;
import org.jetbrains.annotations.NotNull;
import pl.coderion.model.Product;

public final class BarcodeEndpoint implements Handler {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Override
  public void handle(@NotNull final Context context) {

    final String base64 = context.queryParam("base64");
    if (base64 == null) {
      context.status(HttpStatus.NOT_FOUND);
      return;
    }

    final String[] split = base64.split(",");
    final String data = split.length == 2 ? split[1] : split[0];
    final Barcode barcode;
    try {
      barcode = BarcodeDecoder.retrieveBarcode(data);
    } catch (final Exception e) {
      context.status(HttpStatus.NOT_FOUND);
      throw new AssertionError(e);
    }

    final Product product = barcode.getProduct();
    if (product == null) {
      context.status(HttpStatus.NOT_FOUND);
      return;
    }

    context.json(product);
  }
}
