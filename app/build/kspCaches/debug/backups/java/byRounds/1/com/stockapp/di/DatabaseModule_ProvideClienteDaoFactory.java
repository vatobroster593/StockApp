package com.stockapp.di;

import com.stockapp.data.local.StockDatabase;
import com.stockapp.data.local.dao.ClienteDao;
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
public final class DatabaseModule_ProvideClienteDaoFactory implements Factory<ClienteDao> {
  private final Provider<StockDatabase> dbProvider;

  public DatabaseModule_ProvideClienteDaoFactory(Provider<StockDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ClienteDao get() {
    return provideClienteDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideClienteDaoFactory create(
      javax.inject.Provider<StockDatabase> dbProvider) {
    return new DatabaseModule_ProvideClienteDaoFactory(Providers.asDaggerProvider(dbProvider));
  }

  public static DatabaseModule_ProvideClienteDaoFactory create(Provider<StockDatabase> dbProvider) {
    return new DatabaseModule_ProvideClienteDaoFactory(dbProvider);
  }

  public static ClienteDao provideClienteDao(StockDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideClienteDao(db));
  }
}
