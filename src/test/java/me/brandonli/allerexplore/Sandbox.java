package me.brandonli.allerexplore;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public final class Sandbox {

  public static void main(final String[] args) throws IOException {
    final String decoded = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAQAAAAAyCAYAAACztwV0AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAEKSURBVHhe7djBioNAFABB3f//5ySwvsuAaELIpasuEX3jzKmR7I+XDUj6O36BIAGAMAGAMAGAsNM/Afd9P67+zdjcX5et8+Nqbn3vuLtufGv/d/cd6/qz942r/b51jnG1fn0+Zu5X5xpn+57Nj6u5s+efrhufnu/qPKu7+999ry8ACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACNsfL8c1EOMLAMIEAMIEAMIEALK27QkbQ3JWiALd7gAAAABJRU5ErkJggg==";
    final Base64.Decoder decoder = Base64.getDecoder();
    final byte[] imageBytes = decoder.decode(decoded);
    try (final InputStream stream = new ByteArrayInputStream(imageBytes)) {
      final BufferedImage img = ImageIO.read(stream);
      if (img == null) {
        System.out.println("Image is null");
      } else {
        System.out.println("Image is not null");
      }
    }
  }
}
