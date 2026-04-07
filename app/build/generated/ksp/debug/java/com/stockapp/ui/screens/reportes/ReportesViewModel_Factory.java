package com.stockapp.ui.screens.reportes;

import com.stockapp.domain.repository.ClienteRepository;
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
public final class ReportesViewModel_Factory implements Factory<ReportesViewModel> {
  private final Provider<VentaRepository> ventaRepositoryProvider;

  private final Provider<ProductoRepository> productoRepositoryProvider;

  private final Provider<ClienteRepository> clienteRepositoryProvider;

  private final Provider<ProveedorRepository> proveedorRepositoryProvider;

  public ReportesViewModel_Factory(Provider<VentaRepository> ventaRepositoryProvider,
      Provider<ProductoRepository> productoRepositoryProvider,
      Provider<ClienteRepository> clienteRepositoryProvider,
      Provider<ProveedorRepository> proveedorRepositoryProvider) {
    this.ventaRepositoryProvider = ventaRepositoryProvider;
    this.productoRepositoryProvider = productoRepositoryProvider;
    this.clienteRepositoryProvider = clienteRepositoryProvider;
    this.proveedorRepositoryProvider = proveedorRepositoryProvider;
  }

  @Override
  public ReportesViewModel get() {
    return newInstance(ventaRepositoryProvider.get(), productoRepositoryProvider.get(), clienteRepositoryProvider.get(), proveedorRepositoryProvider.get());
  }

  public static ReportesViewModel_Factory create(
      javax.inject.Provider<VentaRepository> ventaRepositoryProvider,
      javax.inject.Provider<ProductoRepository> productoRepositoryProvider,
      javax.inject.Provider<ClienteRepository> clienteRepositoryProvider,
      javax.inject.Provider<ProveedorRepository> proveedorRepositoryProvider) {
    return new ReportesViewModel_Factory(Providers.asDaggerProvider(ventaRepositoryProvider), Providers.asDaggerProvider(productoRepositoryProvider), Providers.asDaggerProvider(clienteRepositoryProvider), Providers.asDaggerProvider(proveedorRepositoryProvider));
  }

  public static ReportesViewModel_Factory create(Provider<VentaRepository> ventaRepositoryProvider,
      Provider<ProductoRepository> productoRepositoryProvider,
      Provider<ClienteRepository> clienteRepositoryProvider,
      Provider<ProveedorRepository> proveedorRepositoryProvider) {
    return new ReportesViewModel_Factory(ventaRepositoryProvider, productoRepositoryProvider, clienteRepositoryProvider, proveedorRepositoryProvider);
  }

  public static ReportesViewModel newInstance(VentaRepository ventaRepository,
      ProductoRepository productoRepository, ClienteRepository clienteRepository,
      ProveedorRepository proveedorRepository) {
    return new ReportesViewModel(ventaRepository, productoRepository, clienteRepository, proveedorRepository);
  }
}
