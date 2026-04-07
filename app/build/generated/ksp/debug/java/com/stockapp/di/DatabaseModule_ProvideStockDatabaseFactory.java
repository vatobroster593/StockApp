package com.stockapp.di;

import android.content.Context;
import com.stockapp.data.local.StockDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideStockDatabaseFactory implements Factory<StockDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideStockDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public StockDatabase get() {
    return provideStockDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideStockDatabaseFactory create(
      javax.inject.Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideStockDatabaseFactory(Providers.asDaggerProvider(contextProvider));
  }

  public static DatabaseModule_ProvideStockDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideStockDatabaseFactory(contextProvider);
  }

  public static StockDatabase provideStockDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideStockDatabase(context));
  }
}
