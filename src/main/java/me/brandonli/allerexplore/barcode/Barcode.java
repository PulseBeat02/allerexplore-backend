package me.brandonli.allerexplore.barcode;

import me.brandonli.allerexplore.product.ProductDecoder;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.coderion.model.Product;

import java.util.Objects;

public final class Barcode {

  private final @Nullable Product product;
  private final String name;
  private final String code;

  Barcode(final String name, final String code) {
    this.name = name;
    this.code = code;
    this.product = ProductDecoder.retrieveProduct(code);
  }

  public static Barcode of(final String name, final String code) {
    return new Barcode(name, code);
  }

  public String getName() {
    return this.name;
  }

  public String getCode() {
    return this.code;
  }

  public @Nullable Product getProduct() {
    return this.product;
  }

  @Override
  public String toString() {
    return "Barcode{" +
        "name='" + this.name + '\'' +
        ", code='" + this.code + '\'' +
        '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.code);
  }
}
