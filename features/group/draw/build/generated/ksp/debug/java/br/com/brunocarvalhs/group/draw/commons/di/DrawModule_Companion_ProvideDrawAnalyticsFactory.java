package br.com.brunocarvalhs.group.draw.commons.di;

import br.com.brunocarvalhs.group.draw.commons.analytics.DrawAnalytics;
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
public final class DrawModule_Companion_ProvideDrawAnalyticsFactory implements Factory<DrawAnalytics> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  private DrawModule_Companion_ProvideDrawAnalyticsFactory(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
  }

  @Override
  public DrawAnalytics get() {
    return provideDrawAnalytics(firebaseAnalyticsProvider.get());
  }

  public static DrawModule_Companion_ProvideDrawAnalyticsFactory create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    return new DrawModule_Companion_ProvideDrawAnalyticsFactory(firebaseAnalyticsProvider);
  }

  public static DrawAnalytics provideDrawAnalytics(FirebaseAnalytics firebaseAnalytics) {
    return Preconditions.checkNotNullFromProvides(DrawModule.Companion.provideDrawAnalytics(firebaseAnalytics));
  }
}
