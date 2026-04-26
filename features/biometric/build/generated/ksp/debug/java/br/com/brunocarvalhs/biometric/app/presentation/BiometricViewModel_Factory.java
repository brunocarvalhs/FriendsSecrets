package br.com.brunocarvalhs.biometric.app.presentation;

import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricUseCase;
import br.com.brunocarvalhs.biometric.commons.analytics.BiometricAnalytics;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
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
public final class BiometricViewModel_Factory implements Factory<BiometricViewModel> {
  private final Provider<BiometricUseCase> biometricUseCaseProvider;

  private final Provider<BiometricAnalytics> analyticsProvider;

  private BiometricViewModel_Factory(Provider<BiometricUseCase> biometricUseCaseProvider,
      Provider<BiometricAnalytics> analyticsProvider) {
    this.biometricUseCaseProvider = biometricUseCaseProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public BiometricViewModel get() {
    return newInstance(biometricUseCaseProvider.get(), analyticsProvider.get());
  }

  public static BiometricViewModel_Factory create(
      Provider<BiometricUseCase> biometricUseCaseProvider,
      Provider<BiometricAnalytics> analyticsProvider) {
    return new BiometricViewModel_Factory(biometricUseCaseProvider, analyticsProvider);
  }

  public static BiometricViewModel newInstance(BiometricUseCase biometricUseCase,
      BiometricAnalytics analytics) {
    return new BiometricViewModel(biometricUseCase, analytics);
  }
}
