package br.com.brunocarvalhs.biometric.commons.flags;

import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.FeatureFlagService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class BiometricFeatureFlags_Factory implements Factory<BiometricFeatureFlags> {
  private final Provider<FeatureFlagService> serviceProvider;

  private BiometricFeatureFlags_Factory(Provider<FeatureFlagService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public BiometricFeatureFlags get() {
    return newInstance(serviceProvider.get());
  }

  public static BiometricFeatureFlags_Factory create(Provider<FeatureFlagService> serviceProvider) {
    return new BiometricFeatureFlags_Factory(serviceProvider);
  }

  public static BiometricFeatureFlags newInstance(FeatureFlagService service) {
    return new BiometricFeatureFlags(service);
  }
}
