package br.com.brunocarvalhs.group.create.commons.analytics;

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
public final class GroupCreateAnalyticsImpl_Factory implements Factory<GroupCreateAnalyticsImpl> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  private GroupCreateAnalyticsImpl_Factory(Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
  }

  @Override
  public GroupCreateAnalyticsImpl get() {
    return newInstance(firebaseAnalyticsProvider.get());
  }

  public static GroupCreateAnalyticsImpl_Factory create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    return new GroupCreateAnalyticsImpl_Factory(firebaseAnalyticsProvider);
  }

  public static GroupCreateAnalyticsImpl newInstance(FirebaseAnalytics firebaseAnalytics) {
    return new GroupCreateAnalyticsImpl(firebaseAnalytics);
  }
}
