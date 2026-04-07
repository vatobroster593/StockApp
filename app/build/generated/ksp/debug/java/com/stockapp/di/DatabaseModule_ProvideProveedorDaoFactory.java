package com.stockapp.di;

import com.stockapp.data.local.StockDatabase;
import com.stockapp.data.local.dao.ProveedorDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideProveedorDaoFactory implements Factory<ProveedorDao> {
  private final Provider<StockDatabase> dbProvider;

  public DatabaseModule_ProvideProveedorDaoFactory(Provider<StockDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ProveedorDao get() {
    return provideProveedorDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideProveedorDaoFactory create(
      javax.inject.Provider<StockDatabase> dbProvider) {
    return new DatabaseModule_ProvideProveedorDaoFactory(Providers.asDaggerProvider(dbProvider));
  }

  public static DatabaseModule_ProvideProveedorDaoFactory create(
      Provider<StockDatabase> dbProvider) {
    return new DatabaseModule_ProvideProveedorDaoFactory(dbProvider);
  }

  public static ProveedorDao provideProveedorDao(StockDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideProveedorDao(db));
  }
}
