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
package me.brandonli.allerexplore.barcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;
import javax.imageio.ImageIO;

public final class BarcodeDecoder {

  private static final Map<DecodeHintType, ?> DECODING_HINTS = Map.of(DecodeHintType.TRY_HARDER, Boolean.TRUE);

  private BarcodeDecoder() {}

  public static Barcode retrieveBarcode(final String base64) throws IOException, NotFoundException {
    final Base64.Decoder decoder = Base64.getDecoder();
    final byte[] imageBytes = decoder.decode(base64);
    try (final InputStream stream = new ByteArrayInputStream(imageBytes)) {
      final BufferedImage img = ImageIO.read(stream);
      return decodeImage(img);
    }
  }

  public static Barcode retrieveBarcode(final Path path) throws IOException, NotFoundException {
    try (final InputStream stream = Files.newInputStream(path)) {
      final BufferedImage img = ImageIO.read(stream);
      return decodeImage(img);
    }
  }

  public static Barcode decodeImage(final BufferedImage image) throws NotFoundException {
    final BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
    final HybridBinarizer binarizer = new HybridBinarizer(source);
    final BinaryBitmap bitmap = new BinaryBitmap(binarizer);
    final Result result = new MultiFormatReader().decode(bitmap, DECODING_HINTS);
    final String text = result.getText();
    final BarcodeFormat format = result.getBarcodeFormat();
    final String name = format.name();
    return Barcode.of(name, text);
  }
}
