package com.stockapp.di;

import com.stockapp.data.local.StockDatabase;
import com.stockapp.data.local.dao.VentaDao;
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
public final class DatabaseModule_ProvideVentaDaoFactory implements Factory<VentaDao> {
  private final Provider<StockDatabase> dbProvider;

  public DatabaseModule_ProvideVentaDaoFactory(Provider<StockDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public VentaDao get() {
    return provideVentaDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideVentaDaoFactory create(
      javax.inject.Provider<StockDatabase> dbProvider) {
    return new DatabaseModule_ProvideVentaDaoFactory(Providers.asDaggerProvider(dbProvider));
  }

  public static DatabaseModule_ProvideVentaDaoFactory create(Provider<StockDatabase> dbProvider) {
    return new DatabaseModule_ProvideVentaDaoFactory(dbProvider);
  }

  public static VentaDao provideVentaDao(StockDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideVentaDao(db));
  }
}
