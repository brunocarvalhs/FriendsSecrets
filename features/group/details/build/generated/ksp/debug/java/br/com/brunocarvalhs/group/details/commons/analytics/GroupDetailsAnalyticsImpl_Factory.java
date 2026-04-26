package br.com.brunocarvalhs.group.details.commons.analytics;

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
public final class GroupDetailsAnalyticsImpl_Factory implements Factory<GroupDetailsAnalyticsImpl> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  private GroupDetailsAnalyticsImpl_Factory(Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
  }

  @Override
  public GroupDetailsAnalyticsImpl get() {
    return newInstance(firebaseAnalyticsProvider.get());
  }

  public static GroupDetailsAnalyticsImpl_Factory create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    return new GroupDetailsAnalyticsImpl_Factory(firebaseAnalyticsProvider);
  }

  public static GroupDetailsAnalyticsImpl newInstance(FirebaseAnalytics firebaseAnalytics) {
    return new GroupDetailsAnalyticsImpl(firebaseAnalytics);
  }
}
