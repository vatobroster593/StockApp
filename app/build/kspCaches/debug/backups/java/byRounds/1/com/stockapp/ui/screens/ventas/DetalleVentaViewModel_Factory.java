package com.stockapp.ui.screens.ventas;

import com.stockapp.domain.repository.VentaRepository;
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
public final class DetalleVentaViewModel_Factory implements Factory<DetalleVentaViewModel> {
  private final Provider<VentaRepository> repositoryProvider;

  public DetalleVentaViewModel_Factory(Provider<VentaRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DetalleVentaViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static DetalleVentaViewModel_Factory create(
      javax.inject.Provider<VentaRepository> repositoryProvider) {
    return new DetalleVentaViewModel_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static DetalleVentaViewModel_Factory create(Provider<VentaRepository> repositoryProvider) {
    return new DetalleVentaViewModel_Factory(repositoryProvider);
  }

  public static DetalleVentaViewModel newInstance(VentaRepository repository) {
    return new DetalleVentaViewModel(repository);
  }
}
