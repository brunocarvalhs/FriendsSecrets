package br.com.brunocarvalhs.group.draw.commons.analytics;

import com.google.firebase.analytics.FirebaseAnalytics;
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
public final class DrawAnalyticsImpl_Factory implements Factory<DrawAnalyticsImpl> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  private DrawAnalyticsImpl_Factory(Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
  }

  @Override
  public DrawAnalyticsImpl get() {
    return newInstance(firebaseAnalyticsProvider.get());
  }

  public static DrawAnalyticsImpl_Factory create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    return new DrawAnalyticsImpl_Factory(firebaseAnalyticsProvider);
  }

  public static DrawAnalyticsImpl newInstance(FirebaseAnalytics firebaseAnalytics) {
    return new DrawAnalyticsImpl(firebaseAnalytics);
  }
}
