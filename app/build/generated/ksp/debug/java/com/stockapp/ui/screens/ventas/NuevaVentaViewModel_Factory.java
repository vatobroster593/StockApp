package com.stockapp.ui.screens.ventas;

import com.stockapp.domain.repository.ClienteRepository;
import com.stockapp.domain.repository.ProductoRepository;
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
public final class NuevaVentaViewModel_Factory implements Factory<NuevaVentaViewModel> {
  private final Provider<VentaRepository> ventaRepositoryProvider;

  private final Provider<ClienteRepository> clienteRepositoryProvider;

  private final Provider<ProductoRepository> productoRepositoryProvider;

  public NuevaVentaViewModel_Factory(Provider<VentaRepository> ventaRepositoryProvider,
      Provider<ClienteRepository> clienteRepositoryProvider,
      Provider<ProductoRepository> productoRepositoryProvider) {
    this.ventaRepositoryProvider = ventaRepositoryProvider;
    this.clienteRepositoryProvider = clienteRepositoryProvider;
    this.productoRepositoryProvider = productoRepositoryProvider;
  }

  @Override
  public NuevaVentaViewModel get() {
    return newInstance(ventaRepositoryProvider.get(), clienteRepositoryProvider.get(), productoRepositoryProvider.get());
  }

  public static NuevaVentaViewModel_Factory create(
      javax.inject.Provider<VentaRepository> ventaRepositoryProvider,
      javax.inject.Provider<ClienteRepository> clienteRepositoryProvider,
      javax.inject.Provider<ProductoRepository> productoRepositoryProvider) {
    return new NuevaVentaViewModel_Factory(Providers.asDaggerProvider(ventaRepositoryProvider), Providers.asDaggerProvider(clienteRepositoryProvider), Providers.asDaggerProvider(productoRepositoryProvider));
  }

  public static NuevaVentaViewModel_Factory create(
      Provider<VentaRepository> ventaRepositoryProvider,
      Provider<ClienteRepository> clienteRepositoryProvider,
      Provider<ProductoRepository> productoRepositoryProvider) {
    return new NuevaVentaViewModel_Factory(ventaRepositoryProvider, clienteRepositoryProvider, productoRepositoryProvider);
  }

  public static NuevaVentaViewModel newInstance(VentaRepository ventaRepository,
      ClienteRepository clienteRepository, ProductoRepository productoRepository) {
    return new NuevaVentaViewModel(ventaRepository, clienteRepository, productoRepository);
  }
}
