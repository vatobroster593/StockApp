package com.stockapp.data.repository;

import com.stockapp.data.local.dao.ProductoDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ProductoRepositoryImpl_Factory implements Factory<ProductoRepositoryImpl> {
  private final Provider<ProductoDao> daoProvider;

  public ProductoRepositoryImpl_Factory(Provider<ProductoDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ProductoRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static ProductoRepositoryImpl_Factory create(
      javax.inject.Provider<ProductoDao> daoProvider) {
    return new ProductoRepositoryImpl_Factory(Providers.asDaggerProvider(daoProvider));
  }

  public static ProductoRepositoryImpl_Factory create(Provider<ProductoDao> daoProvider) {
    return new ProductoRepositoryImpl_Factory(daoProvider);
  }

  public static ProductoRepositoryImpl newInstance(ProductoDao dao) {
    return new ProductoRepositoryImpl(dao);
  }
}
