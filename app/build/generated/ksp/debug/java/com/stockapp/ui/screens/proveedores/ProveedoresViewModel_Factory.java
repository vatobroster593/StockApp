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
public final class ProveedoresViewModel_Factory implements Factory<ProveedoresViewModel> {
  private final Provider<ProveedorRepository> repositoryProvider;

  public ProveedoresViewModel_Factory(Provider<ProveedorRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ProveedoresViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ProveedoresViewModel_Factory create(
      javax.inject.Provider<ProveedorRepository> repositoryProvider) {
    return new ProveedoresViewModel_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static ProveedoresViewModel_Factory create(
      Provider<ProveedorRepository> repositoryProvider) {
    return new ProveedoresViewModel_Factory(repositoryProvider);
  }

  public static ProveedoresViewModel newInstance(ProveedorRepository repository) {
    return new ProveedoresViewModel(repository);
  }
}
