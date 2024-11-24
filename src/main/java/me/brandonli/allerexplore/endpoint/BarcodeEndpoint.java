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
