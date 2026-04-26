package br.com.brunocarvalhs.biometric.app.domain.useCases;

import br.com.brunocarvalhs.biometric.BiometricService;
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
public final class IsBiometricEnabledUseCase_Factory implements Factory<IsBiometricEnabledUseCase> {
  private final Provider<BiometricService> serviceProvider;

  private IsBiometricEnabledUseCase_Factory(Provider<BiometricService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public IsBiometricEnabledUseCase get() {
    return newInstance(serviceProvider.get());
  }

  public static IsBiometricEnabledUseCase_Factory create(
      Provider<BiometricService> serviceProvider) {
    return new IsBiometricEnabledUseCase_Factory(serviceProvider);
  }

  public static IsBiometricEnabledUseCase newInstance(BiometricService service) {
    return new IsBiometricEnabledUseCase(service);
  }
}
