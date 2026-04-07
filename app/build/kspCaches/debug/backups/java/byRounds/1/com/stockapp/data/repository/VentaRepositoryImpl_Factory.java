package com.stockapp.data.repository;

import com.stockapp.data.local.StockDatabase;
import com.stockapp.data.local.dao.ProductoDao;
import com.stockapp.data.local.dao.VentaDao;
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
public final class VentaRepositoryImpl_Factory implements Factory<VentaRepositoryImpl> {
  private final Provider<StockDatabase> databaseProvider;

  private final Provider<VentaDao> ventaDaoProvider;

  private final Provider<ProductoDao> productoDaoProvider;

  public VentaRepositoryImpl_Factory(Provider<StockDatabase> databaseProvider,
      Provider<VentaDao> ventaDaoProvider, Provider<ProductoDao> productoDaoProvider) {
    this.databaseProvider = databaseProvider;
    this.ventaDaoProvider = ventaDaoProvider;
    this.productoDaoProvider = productoDaoProvider;
  }

  @Override
  public VentaRepositoryImpl get() {
    return newInstance(databaseProvider.get(), ventaDaoProvider.get(), productoDaoProvider.get());
  }

  public static VentaRepositoryImpl_Factory create(
      javax.inject.Provider<StockDatabase> databaseProvider,
      javax.inject.Provider<VentaDao> ventaDaoProvider,
      javax.inject.Provider<ProductoDao> productoDaoProvider) {
    return new VentaRepositoryImpl_Factory(Providers.asDaggerProvider(databaseProvider), Providers.asDaggerProvider(ventaDaoProvider), Providers.asDaggerProvider(productoDaoProvider));
  }

  public static VentaRepositoryImpl_Factory create(Provider<StockDatabase> databaseProvider,
      Provider<VentaDao> ventaDaoProvider, Provider<ProductoDao> productoDaoProvider) {
    return new VentaRepositoryImpl_Factory(databaseProvider, ventaDaoProvider, productoDaoProvider);
  }

  public static VentaRepositoryImpl newInstance(StockDatabase database, VentaDao ventaDao,
      ProductoDao productoDao) {
    return new VentaRepositoryImpl(database, ventaDao, productoDao);
  }
}
