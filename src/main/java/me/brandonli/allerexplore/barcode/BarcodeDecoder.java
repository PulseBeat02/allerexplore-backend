package me.brandonli.allerexplore.barcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import jakarta.xml.bind.DatatypeConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;

public final class BarcodeDecoder {

  private static final Map<DecodeHintType, ?> DECODING_HINTS = Map.of(DecodeHintType.TRY_HARDER, Boolean.TRUE);

  private BarcodeDecoder() {
  }

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
