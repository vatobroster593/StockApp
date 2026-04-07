package com.stockapp.data.repository;

import com.stockapp.data.local.StockDatabase;
import com.stockapp.data.local.dao.ProveedorDao;
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
public final class ProveedorRepositoryImpl_Factory implements Factory<ProveedorRepositoryImpl> {
  private final Provider<StockDatabase> databaseProvider;

  private final Provider<ProveedorDao> proveedorDaoProvider;

  public ProveedorRepositoryImpl_Factory(Provider<StockDatabase> databaseProvider,
      Provider<ProveedorDao> proveedorDaoProvider) {
    this.databaseProvider = databaseProvider;
    this.proveedorDaoProvider = proveedorDaoProvider;
  }

  @Override
  public ProveedorRepositoryImpl get() {
    return newInstance(databaseProvider.get(), proveedorDaoProvider.get());
  }

  public static ProveedorRepositoryImpl_Factory create(
      javax.inject.Provider<StockDatabase> databaseProvider,
      javax.inject.Provider<ProveedorDao> proveedorDaoProvider) {
    return new ProveedorRepositoryImpl_Factory(Providers.asDaggerProvider(databaseProvider), Providers.asDaggerProvider(proveedorDaoProvider));
  }

  public static ProveedorRepositoryImpl_Factory create(Provider<StockDatabase> databaseProvider,
      Provider<ProveedorDao> proveedorDaoProvider) {
    return new ProveedorRepositoryImpl_Factory(databaseProvider, proveedorDaoProvider);
  }

  public static ProveedorRepositoryImpl newInstance(StockDatabase database,
      ProveedorDao proveedorDao) {
    return new ProveedorRepositoryImpl(database, proveedorDao);
  }
}
