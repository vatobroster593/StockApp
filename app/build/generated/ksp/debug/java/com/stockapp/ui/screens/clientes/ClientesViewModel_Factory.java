package com.stockapp.ui.screens.clientes;

import com.stockapp.domain.repository.ClienteRepository;
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
public final class ClientesViewModel_Factory implements Factory<ClientesViewModel> {
  private final Provider<ClienteRepository> repositoryProvider;

  public ClientesViewModel_Factory(Provider<ClienteRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ClientesViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ClientesViewModel_Factory create(
      javax.inject.Provider<ClienteRepository> repositoryProvider) {
    return new ClientesViewModel_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static ClientesViewModel_Factory create(Provider<ClienteRepository> repositoryProvider) {
    return new ClientesViewModel_Factory(repositoryProvider);
  }

  public static ClientesViewModel newInstance(ClienteRepository repository) {
    return new ClientesViewModel(repository);
  }
}
