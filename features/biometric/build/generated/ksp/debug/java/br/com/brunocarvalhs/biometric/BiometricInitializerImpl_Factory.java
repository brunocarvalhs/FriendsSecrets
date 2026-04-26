package br.com.brunocarvalhs.biometric;

import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator;
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
public final class BiometricInitializerImpl_Factory implements Factory<BiometricInitializerImpl> {
  private final Provider<CommonNavigator> commonNavigatorProvider;

  private BiometricInitializerImpl_Factory(Provider<CommonNavigator> commonNavigatorProvider) {
    this.commonNavigatorProvider = commonNavigatorProvider;
  }

  @Override
  public BiometricInitializerImpl get() {
    return newInstance(commonNavigatorProvider.get());
  }

  public static BiometricInitializerImpl_Factory create(
      Provider<CommonNavigator> commonNavigatorProvider) {
    return new BiometricInitializerImpl_Factory(commonNavigatorProvider);
  }

  public static BiometricInitializerImpl newInstance(CommonNavigator commonNavigator) {
    return new BiometricInitializerImpl(commonNavigator);
  }
}
