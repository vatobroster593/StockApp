package com.stockapp.ui.screens.inventario;

import com.stockapp.domain.repository.ProductoRepository;
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
public final class AgregarEditarProductoViewModel_Factory implements Factory<AgregarEditarProductoViewModel> {
  private final Provider<ProductoRepository> repositoryProvider;

  public AgregarEditarProductoViewModel_Factory(Provider<ProductoRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AgregarEditarProductoViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AgregarEditarProductoViewModel_Factory create(
      javax.inject.Provider<ProductoRepository> repositoryProvider) {
    return new AgregarEditarProductoViewModel_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static AgregarEditarProductoViewModel_Factory create(
      Provider<ProductoRepository> repositoryProvider) {
    return new AgregarEditarProductoViewModel_Factory(repositoryProvider);
  }

  public static AgregarEditarProductoViewModel newInstance(ProductoRepository repository) {
    return new AgregarEditarProductoViewModel(repository);
  }
}
