package com.stockapp;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.stockapp.data.local.StockDatabase;
import com.stockapp.data.local.dao.ClienteDao;
import com.stockapp.data.local.dao.ProductoDao;
import com.stockapp.data.local.dao.ProveedorDao;
import com.stockapp.data.local.dao.VentaDao;
import com.stockapp.data.repository.ClienteRepositoryImpl;
import com.stockapp.data.repository.ProductoRepositoryImpl;
import com.stockapp.data.repository.ProveedorRepositoryImpl;
import com.stockapp.data.repository.VentaRepositoryImpl;
import com.stockapp.di.DatabaseModule_ProvideClienteDaoFactory;
import com.stockapp.di.DatabaseModule_ProvideProductoDaoFactory;
import com.stockapp.di.DatabaseModule_ProvideProveedorDaoFactory;
import com.stockapp.di.DatabaseModule_ProvideStockDatabaseFactory;
import com.stockapp.di.DatabaseModule_ProvideVentaDaoFactory;
import com.stockapp.domain.repository.ClienteRepository;
import com.stockapp.domain.repository.ProductoRepository;
import com.stockapp.domain.repository.ProveedorRepository;
import com.stockapp.domain.repository.VentaRepository;
import com.stockapp.ui.screens.clientes.AgregarEditarClienteViewModel;
import com.stockapp.ui.screens.clientes.AgregarEditarClienteViewModel_HiltModules;
import com.stockapp.ui.screens.clientes.AgregarEditarClienteViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.stockapp.ui.screens.clientes.AgregarEditarClienteViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.stockapp.ui.screens.clientes.ClientesViewModel;
import com.stockapp.ui.screens.clientes.ClientesViewModel_HiltModules;
import com.stockapp.ui.screens.clientes.ClientesViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.stockapp.ui.screens.clientes.ClientesViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.stockapp.ui.screens.inventario.AgregarEditarProductoViewModel;
import com.stockapp.ui.screens.inventario.AgregarEditarProductoViewModel_HiltModules;
import com.stockapp.ui.screens.inventario.AgregarEditarProductoViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.stockapp.ui.screens.inventario.AgregarEditarProductoViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.stockapp.ui.screens.inventario.InventarioViewModel;
import com.stockapp.ui.screens.inventario.InventarioViewModel_HiltModules;
import com.stockapp.ui.screens.inventario.InventarioViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.stockapp.ui.screens.inventario.InventarioViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.stockapp.ui.screens.proveedores.AgregarEditarProveedorViewModel;
import com.stockapp.ui.screens.proveedores.AgregarEditarProveedorViewModel_HiltModules;
import com.stockapp.ui.screens.proveedores.AgregarEditarProveedorViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.stockapp.ui.screens.proveedores.AgregarEditarProveedorViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.stockapp.ui.screens.proveedores.DetalleProveedorViewModel;
import com.stockapp.ui.screens.proveedores.DetalleProveedorViewModel_HiltModules;
import com.stockapp.ui.screens.proveedores.DetalleProveedorViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.stockapp.ui.screens.proveedores.DetalleProveedorViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.stockapp.ui.screens.proveedores.NuevaCompraViewModel;
import com.stockapp.ui.screens.proveedores.NuevaCompraViewModel_HiltModules;
import com.stockapp.ui.screens.proveedores.NuevaCompraViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.stockapp.ui.screens.proveedores.NuevaCompraViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.stockapp.ui.screens.proveedores.ProveedoresViewModel;
import com.stockapp.ui.screens.proveedores.ProveedoresViewModel_HiltModules;
import com.stockapp.ui.screens.proveedores.ProveedoresViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.stockapp.ui.screens.proveedores.ProveedoresViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.stockapp.ui.screens.ventas.DetalleVentaViewModel;
import com.stockapp.ui.screens.ventas.DetalleVentaViewModel_HiltModules;
import com.stockapp.ui.screens.ventas.DetalleVentaViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.stockapp.ui.screens.ventas.DetalleVentaViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.stockapp.ui.screens.ventas.NuevaVentaViewModel;
import com.stockapp.ui.screens.ventas.NuevaVentaViewModel_HiltModules;
import com.stockapp.ui.screens.ventas.NuevaVentaViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.stockapp.ui.screens.ventas.NuevaVentaViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.stockapp.ui.screens.ventas.VentasViewModel;
import com.stockapp.ui.screens.ventas.VentasViewModel_HiltModules;
import com.stockapp.ui.screens.ventas.VentasViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.stockapp.ui.screens.ventas.VentasViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerStockApp_HiltComponents_SingletonC {
  private DaggerStockApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public StockApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements StockApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public StockApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements StockApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public StockApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements StockApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public StockApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements StockApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public StockApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements StockApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public StockApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements StockApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public StockApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements StockApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public StockApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends StockApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends StockApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends StockApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends StockApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(11).put(AgregarEditarClienteViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AgregarEditarClienteViewModel_HiltModules.KeyModule.provide()).put(AgregarEditarProductoViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AgregarEditarProductoViewModel_HiltModules.KeyModule.provide()).put(AgregarEditarProveedorViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AgregarEditarProveedorViewModel_HiltModules.KeyModule.provide()).put(ClientesViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ClientesViewModel_HiltModules.KeyModule.provide()).put(DetalleProveedorViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, DetalleProveedorViewModel_HiltModules.KeyModule.provide()).put(DetalleVentaViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, DetalleVentaViewModel_HiltModules.KeyModule.provide()).put(InventarioViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, InventarioViewModel_HiltModules.KeyModule.provide()).put(NuevaCompraViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, NuevaCompraViewModel_HiltModules.KeyModule.provide()).put(NuevaVentaViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, NuevaVentaViewModel_HiltModules.KeyModule.provide()).put(ProveedoresViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ProveedoresViewModel_HiltModules.KeyModule.provide()).put(VentasViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, VentasViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends StockApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AgregarEditarClienteViewModel> agregarEditarClienteViewModelProvider;

    private Provider<AgregarEditarProductoViewModel> agregarEditarProductoViewModelProvider;

    private Provider<AgregarEditarProveedorViewModel> agregarEditarProveedorViewModelProvider;

    private Provider<ClientesViewModel> clientesViewModelProvider;

    private Provider<DetalleProveedorViewModel> detalleProveedorViewModelProvider;

    private Provider<DetalleVentaViewModel> detalleVentaViewModelProvider;

    private Provider<InventarioViewModel> inventarioViewModelProvider;

    private Provider<NuevaCompraViewModel> nuevaCompraViewModelProvider;

    private Provider<NuevaVentaViewModel> nuevaVentaViewModelProvider;

    private Provider<ProveedoresViewModel> proveedoresViewModelProvider;

    private Provider<VentasViewModel> ventasViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.agregarEditarClienteViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.agregarEditarProductoViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.agregarEditarProveedorViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.clientesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.detalleProveedorViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.detalleVentaViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.inventarioViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.nuevaCompraViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.nuevaVentaViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.proveedoresViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.ventasViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(11).put(AgregarEditarClienteViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) agregarEditarClienteViewModelProvider)).put(AgregarEditarProductoViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) agregarEditarProductoViewModelProvider)).put(AgregarEditarProveedorViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) agregarEditarProveedorViewModelProvider)).put(ClientesViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) clientesViewModelProvider)).put(DetalleProveedorViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) detalleProveedorViewModelProvider)).put(DetalleVentaViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) detalleVentaViewModelProvider)).put(InventarioViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) inventarioViewModelProvider)).put(NuevaCompraViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) nuevaCompraViewModelProvider)).put(NuevaVentaViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) nuevaVentaViewModelProvider)).put(ProveedoresViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) proveedoresViewModelProvider)).put(VentasViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) ventasViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.stockapp.ui.screens.clientes.AgregarEditarClienteViewModel 
          return (T) new AgregarEditarClienteViewModel(singletonCImpl.bindClienteRepositoryProvider.get());

          case 1: // com.stockapp.ui.screens.inventario.AgregarEditarProductoViewModel 
          return (T) new AgregarEditarProductoViewModel(singletonCImpl.bindProductoRepositoryProvider.get());

          case 2: // com.stockapp.ui.screens.proveedores.AgregarEditarProveedorViewModel 
          return (T) new AgregarEditarProveedorViewModel(singletonCImpl.bindProveedorRepositoryProvider.get());

          case 3: // com.stockapp.ui.screens.clientes.ClientesViewModel 
          return (T) new ClientesViewModel(singletonCImpl.bindClienteRepositoryProvider.get());

          case 4: // com.stockapp.ui.screens.proveedores.DetalleProveedorViewModel 
          return (T) new DetalleProveedorViewModel(singletonCImpl.bindProveedorRepositoryProvider.get());

          case 5: // com.stockapp.ui.screens.ventas.DetalleVentaViewModel 
          return (T) new DetalleVentaViewModel(singletonCImpl.bindVentaRepositoryProvider.get());

          case 6: // com.stockapp.ui.screens.inventario.InventarioViewModel 
          return (T) new InventarioViewModel(singletonCImpl.bindProductoRepositoryProvider.get());

          case 7: // com.stockapp.ui.screens.proveedores.NuevaCompraViewModel 
          return (T) new NuevaCompraViewModel(singletonCImpl.bindProveedorRepositoryProvider.get(), viewModelCImpl.savedStateHandle);

          case 8: // com.stockapp.ui.screens.ventas.NuevaVentaViewModel 
          return (T) new NuevaVentaViewModel(singletonCImpl.bindVentaRepositoryProvider.get(), singletonCImpl.bindClienteRepositoryProvider.get(), singletonCImpl.bindProductoRepositoryProvider.get());

          case 9: // com.stockapp.ui.screens.proveedores.ProveedoresViewModel 
          return (T) new ProveedoresViewModel(singletonCImpl.bindProveedorRepositoryProvider.get());

          case 10: // com.stockapp.ui.screens.ventas.VentasViewModel 
          return (T) new VentasViewModel(singletonCImpl.bindVentaRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends StockApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends StockApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends StockApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<StockDatabase> provideStockDatabaseProvider;

    private Provider<ClienteRepositoryImpl> clienteRepositoryImplProvider;

    private Provider<ClienteRepository> bindClienteRepositoryProvider;

    private Provider<ProductoRepositoryImpl> productoRepositoryImplProvider;

    private Provider<ProductoRepository> bindProductoRepositoryProvider;

    private Provider<ProveedorRepositoryImpl> proveedorRepositoryImplProvider;

    private Provider<ProveedorRepository> bindProveedorRepositoryProvider;

    private Provider<VentaRepositoryImpl> ventaRepositoryImplProvider;

    private Provider<VentaRepository> bindVentaRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private ClienteDao clienteDao() {
      return DatabaseModule_ProvideClienteDaoFactory.provideClienteDao(provideStockDatabaseProvider.get());
    }

    private ProductoDao productoDao() {
      return DatabaseModule_ProvideProductoDaoFactory.provideProductoDao(provideStockDatabaseProvider.get());
    }

    private ProveedorDao proveedorDao() {
      return DatabaseModule_ProvideProveedorDaoFactory.provideProveedorDao(provideStockDatabaseProvider.get());
    }

    private VentaDao ventaDao() {
      return DatabaseModule_ProvideVentaDaoFactory.provideVentaDao(provideStockDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideStockDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<StockDatabase>(singletonCImpl, 1));
      this.clienteRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 0);
      this.bindClienteRepositoryProvider = DoubleCheck.provider((Provider) clienteRepositoryImplProvider);
      this.productoRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 2);
      this.bindProductoRepositoryProvider = DoubleCheck.provider((Provider) productoRepositoryImplProvider);
      this.proveedorRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 3);
      this.bindProveedorRepositoryProvider = DoubleCheck.provider((Provider) proveedorRepositoryImplProvider);
      this.ventaRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 4);
      this.bindVentaRepositoryProvider = DoubleCheck.provider((Provider) ventaRepositoryImplProvider);
    }

    @Override
    public void injectStockApp(StockApp stockApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.stockapp.data.repository.ClienteRepositoryImpl 
          return (T) new ClienteRepositoryImpl(singletonCImpl.clienteDao());

          case 1: // com.stockapp.data.local.StockDatabase 
          return (T) DatabaseModule_ProvideStockDatabaseFactory.provideStockDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.stockapp.data.repository.ProductoRepositoryImpl 
          return (T) new ProductoRepositoryImpl(singletonCImpl.productoDao());

          case 3: // com.stockapp.data.repository.ProveedorRepositoryImpl 
          return (T) new ProveedorRepositoryImpl(singletonCImpl.provideStockDatabaseProvider.get(), singletonCImpl.proveedorDao());

          case 4: // com.stockapp.data.repository.VentaRepositoryImpl 
          return (T) new VentaRepositoryImpl(singletonCImpl.provideStockDatabaseProvider.get(), singletonCImpl.ventaDao(), singletonCImpl.productoDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
