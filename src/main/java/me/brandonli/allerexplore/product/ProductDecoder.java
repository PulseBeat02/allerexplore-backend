package me.brandonli.allerexplore.product;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.checkerframework.checker.nullness.qual.Nullable;
import pl.coderion.model.Product;
import pl.coderion.model.ProductResponse;
import pl.coderion.service.OpenFoodFactsWrapper;
import pl.coderion.service.impl.OpenFoodFactsWrapperImpl;

import java.util.concurrent.TimeUnit;

public final class ProductDecoder {

  private static final OpenFoodFactsWrapper WRAPPER = new OpenFoodFactsWrapperImpl();
  private static final LoadingCache<String, ProductResponse> PRODUCT_CACHE = Caffeine.newBuilder()
          .expireAfterWrite(4, TimeUnit.HOURS)
          .maximumSize(128_000)
          .build(WRAPPER::fetchProductByCode);

  private ProductDecoder() {
  }

  public static @Nullable Product retrieveProduct(final String code) {
    final ProductResponse response = PRODUCT_CACHE.get(code);
    return response != null ? response.getProduct() : null;
  }
}