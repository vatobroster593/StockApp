package com.stockapp.data.repository;

import com.stockapp.data.local.dao.ClienteDao;
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
public final class ClienteRepositoryImpl_Factory implements Factory<ClienteRepositoryImpl> {
  private final Provider<ClienteDao> daoProvider;

  public ClienteRepositoryImpl_Factory(Provider<ClienteDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ClienteRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static ClienteRepositoryImpl_Factory create(
      javax.inject.Provider<ClienteDao> daoProvider) {
    return new ClienteRepositoryImpl_Factory(Providers.asDaggerProvider(daoProvider));
  }

  public static ClienteRepositoryImpl_Factory create(Provider<ClienteDao> daoProvider) {
    return new ClienteRepositoryImpl_Factory(daoProvider);
  }

  public static ClienteRepositoryImpl newInstance(ClienteDao dao) {
    return new ClienteRepositoryImpl(dao);
  }
}
