package br.com.brunocarvalhs.biometric.commons.di;

import br.com.brunocarvalhs.biometric.commons.analytics.BiometricAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class BiometricModule_Companion_ProvideBiometricAnalyticsFactory implements Factory<BiometricAnalytics> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  private BiometricModule_Companion_ProvideBiometricAnalyticsFactory(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
  }

  @Override
  public BiometricAnalytics get() {
    return provideBiometricAnalytics(firebaseAnalyticsProvider.get());
  }

  public static BiometricModule_Companion_ProvideBiometricAnalyticsFactory create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    return new BiometricModule_Companion_ProvideBiometricAnalyticsFactory(firebaseAnalyticsProvider);
  }

  public static BiometricAnalytics provideBiometricAnalytics(FirebaseAnalytics firebaseAnalytics) {
    return Preconditions.checkNotNullFromProvides(BiometricModule.Companion.provideBiometricAnalytics(firebaseAnalytics));
  }
}
