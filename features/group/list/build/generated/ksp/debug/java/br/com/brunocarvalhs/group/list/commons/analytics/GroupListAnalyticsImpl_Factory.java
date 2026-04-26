package br.com.brunocarvalhs.group.list.commons.analytics;

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
public final class GroupListAnalyticsImpl_Factory implements Factory<GroupListAnalyticsImpl> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  private GroupListAnalyticsImpl_Factory(Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
  }

  @Override
  public GroupListAnalyticsImpl get() {
    return newInstance(firebaseAnalyticsProvider.get());
  }

  public static GroupListAnalyticsImpl_Factory create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    return new GroupListAnalyticsImpl_Factory(firebaseAnalyticsProvider);
  }

  public static GroupListAnalyticsImpl newInstance(FirebaseAnalytics firebaseAnalytics) {
    return new GroupListAnalyticsImpl(firebaseAnalytics);
  }
}
