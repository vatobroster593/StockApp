package com.stockapp.ui.screens.proveedores;

import androidx.lifecycle.SavedStateHandle;
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
public final class NuevaCompraViewModel_Factory implements Factory<NuevaCompraViewModel> {
  private final Provider<ProveedorRepository> repositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public NuevaCompraViewModel_Factory(Provider<ProveedorRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public NuevaCompraViewModel get() {
    return newInstance(repositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static NuevaCompraViewModel_Factory create(
      javax.inject.Provider<ProveedorRepository> repositoryProvider,
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider) {
    return new NuevaCompraViewModel_Factory(Providers.asDaggerProvider(repositoryProvider), Providers.asDaggerProvider(savedStateHandleProvider));
  }

  public static NuevaCompraViewModel_Factory create(
      Provider<ProveedorRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new NuevaCompraViewModel_Factory(repositoryProvider, savedStateHandleProvider);
  }

  public static NuevaCompraViewModel newInstance(ProveedorRepository repository,
      SavedStateHandle savedStateHandle) {
    return new NuevaCompraViewModel(repository, savedStateHandle);
  }
}
