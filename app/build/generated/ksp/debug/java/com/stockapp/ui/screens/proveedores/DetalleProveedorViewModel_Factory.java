package com.stockapp.ui.screens.proveedores;

import com.stockapp.domain.repository.ProveedorRepository;
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
public final class DetalleProveedorViewModel_Factory implements Factory<DetalleProveedorViewModel> {
  private final Provider<ProveedorRepository> repositoryProvider;

  public DetalleProveedorViewModel_Factory(Provider<ProveedorRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DetalleProveedorViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static DetalleProveedorViewModel_Factory create(
      javax.inject.Provider<ProveedorRepository> repositoryProvider) {
    return new DetalleProveedorViewModel_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static DetalleProveedorViewModel_Factory create(
      Provider<ProveedorRepository> repositoryProvider) {
    return new DetalleProveedorViewModel_Factory(repositoryProvider);
  }

  public static DetalleProveedorViewModel newInstance(ProveedorRepository repository) {
    return new DetalleProveedorViewModel(repository);
  }
}
