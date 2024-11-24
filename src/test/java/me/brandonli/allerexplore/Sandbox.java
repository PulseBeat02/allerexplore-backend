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
package me.brandonli.allerexplore;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

public final class Sandbox {

  public static void main(final String[] args) throws IOException {
    final String decoded =
      "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAQAAAAAyCAYAAACztwV0AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAEKSURBVHhe7djBioNAFABB3f//5ySwvsuAaELIpasuEX3jzKmR7I+XDUj6O36BIAGAMAGAMAGAsNM/Afd9P67+zdjcX5et8+Nqbn3vuLtufGv/d/cd6/qz942r/b51jnG1fn0+Zu5X5xpn+57Nj6u5s+efrhufnu/qPKu7+999ry8ACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACBMACNsfL8c1EOMLAMIEAMIEAMIEALK27QkbQ3JWiALd7gAAAABJRU5ErkJggg==";
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
