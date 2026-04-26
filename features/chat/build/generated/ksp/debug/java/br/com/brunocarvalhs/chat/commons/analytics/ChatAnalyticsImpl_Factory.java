package br.com.brunocarvalhs.chat.commons.analytics;

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
public final class ChatAnalyticsImpl_Factory implements Factory<ChatAnalyticsImpl> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  private ChatAnalyticsImpl_Factory(Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
  }

  @Override
  public ChatAnalyticsImpl get() {
    return newInstance(firebaseAnalyticsProvider.get());
  }

  public static ChatAnalyticsImpl_Factory create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    return new ChatAnalyticsImpl_Factory(firebaseAnalyticsProvider);
  }

  public static ChatAnalyticsImpl newInstance(FirebaseAnalytics firebaseAnalytics) {
    return new ChatAnalyticsImpl(firebaseAnalytics);
  }
}
