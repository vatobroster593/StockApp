package com.stockapp.ui.screens.ajustes;

import com.stockapp.data.preferences.AppPreferences;
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
public final class AjustesViewModel_Factory implements Factory<AjustesViewModel> {
  private final Provider<AppPreferences> prefsProvider;

  public AjustesViewModel_Factory(Provider<AppPreferences> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public AjustesViewModel get() {
    return newInstance(prefsProvider.get());
  }

  public static AjustesViewModel_Factory create(
      javax.inject.Provider<AppPreferences> prefsProvider) {
    return new AjustesViewModel_Factory(Providers.asDaggerProvider(prefsProvider));
  }

  public static AjustesViewModel_Factory create(Provider<AppPreferences> prefsProvider) {
    return new AjustesViewModel_Factory(prefsProvider);
  }

  public static AjustesViewModel newInstance(AppPreferences prefs) {
    return new AjustesViewModel(prefs);
  }
}
