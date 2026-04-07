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
public final class AgregarEditarClienteViewModel_Factory implements Factory<AgregarEditarClienteViewModel> {
  private final Provider<ClienteRepository> repositoryProvider;

  public AgregarEditarClienteViewModel_Factory(Provider<ClienteRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AgregarEditarClienteViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AgregarEditarClienteViewModel_Factory create(
      javax.inject.Provider<ClienteRepository> repositoryProvider) {
    return new AgregarEditarClienteViewModel_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static AgregarEditarClienteViewModel_Factory create(
      Provider<ClienteRepository> repositoryProvider) {
    return new AgregarEditarClienteViewModel_Factory(repositoryProvider);
  }

  public static AgregarEditarClienteViewModel newInstance(ClienteRepository repository) {
    return new AgregarEditarClienteViewModel(repository);
  }
}
