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
public final class BiometricUseCase_Factory implements Factory<BiometricUseCase> {
  private final Provider<BiometricService> biometricManagerProvider;

  private BiometricUseCase_Factory(Provider<BiometricService> biometricManagerProvider) {
    this.biometricManagerProvider = biometricManagerProvider;
  }

  @Override
  public BiometricUseCase get() {
    return newInstance(biometricManagerProvider.get());
  }

  public static BiometricUseCase_Factory create(
      Provider<BiometricService> biometricManagerProvider) {
    return new BiometricUseCase_Factory(biometricManagerProvider);
  }

  public static BiometricUseCase newInstance(BiometricService biometricManager) {
    return new BiometricUseCase(biometricManager);
  }
}
