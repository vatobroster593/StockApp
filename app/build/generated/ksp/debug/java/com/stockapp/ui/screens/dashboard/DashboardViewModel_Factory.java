package com.stockapp.ui.screens.dashboard;

import com.stockapp.data.preferences.AppPreferences;
import com.stockapp.domain.repository.ProductoRepository;
import com.stockapp.domain.repository.ProveedorRepository;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<VentaRepository> ventaRepositoryProvider;

  private final Provider<ProveedorRepository> proveedorRepositoryProvider;

  private final Provider<ProductoRepository> productoRepositoryProvider;

  private final Provider<AppPreferences> prefsProvider;

  public DashboardViewModel_Factory(Provider<VentaRepository> ventaRepositoryProvider,
      Provider<ProveedorRepository> proveedorRepositoryProvider,
      Provider<ProductoRepository> productoRepositoryProvider,
      Provider<AppPreferences> prefsProvider) {
    this.ventaRepositoryProvider = ventaRepositoryProvider;
    this.proveedorRepositoryProvider = proveedorRepositoryProvider;
    this.productoRepositoryProvider = productoRepositoryProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(ventaRepositoryProvider.get(), proveedorRepositoryProvider.get(), productoRepositoryProvider.get(), prefsProvider.get());
  }

  public static DashboardViewModel_Factory create(
      javax.inject.Provider<VentaRepository> ventaRepositoryProvider,
      javax.inject.Provider<ProveedorRepository> proveedorRepositoryProvider,
      javax.inject.Provider<ProductoRepository> productoRepositoryProvider,
      javax.inject.Provider<AppPreferences> prefsProvider) {
    return new DashboardViewModel_Factory(Providers.asDaggerProvider(ventaRepositoryProvider), Providers.asDaggerProvider(proveedorRepositoryProvider), Providers.asDaggerProvider(productoRepositoryProvider), Providers.asDaggerProvider(prefsProvider));
  }

  public static DashboardViewModel_Factory create(Provider<VentaRepository> ventaRepositoryProvider,
      Provider<ProveedorRepository> proveedorRepositoryProvider,
      Provider<ProductoRepository> productoRepositoryProvider,
      Provider<AppPreferences> prefsProvider) {
    return new DashboardViewModel_Factory(ventaRepositoryProvider, proveedorRepositoryProvider, productoRepositoryProvider, prefsProvider);
  }

  public static DashboardViewModel newInstance(VentaRepository ventaRepository,
      ProveedorRepository proveedorRepository, ProductoRepository productoRepository,
      AppPreferences prefs) {
    return new DashboardViewModel(ventaRepository, proveedorRepository, productoRepository, prefs);
  }
}
