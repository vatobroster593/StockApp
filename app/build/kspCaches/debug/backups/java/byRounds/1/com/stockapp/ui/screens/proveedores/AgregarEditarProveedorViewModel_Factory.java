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
public final class AgregarEditarProveedorViewModel_Factory implements Factory<AgregarEditarProveedorViewModel> {
  private final Provider<ProveedorRepository> repositoryProvider;

  public AgregarEditarProveedorViewModel_Factory(Provider<ProveedorRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AgregarEditarProveedorViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AgregarEditarProveedorViewModel_Factory create(
      javax.inject.Provider<ProveedorRepository> repositoryProvider) {
    return new AgregarEditarProveedorViewModel_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static AgregarEditarProveedorViewModel_Factory create(
      Provider<ProveedorRepository> repositoryProvider) {
    return new AgregarEditarProveedorViewModel_Factory(repositoryProvider);
  }

  public static AgregarEditarProveedorViewModel newInstance(ProveedorRepository repository) {
    return new AgregarEditarProveedorViewModel(repository);
  }
}
