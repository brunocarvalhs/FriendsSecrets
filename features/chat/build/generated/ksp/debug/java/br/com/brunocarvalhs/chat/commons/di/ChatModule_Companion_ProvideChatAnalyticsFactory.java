package br.com.brunocarvalhs.chat.commons.di;

import br.com.brunocarvalhs.chat.commons.analytics.ChatAnalytics;
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
public final class ChatModule_Companion_ProvideChatAnalyticsFactory implements Factory<ChatAnalytics> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  private ChatModule_Companion_ProvideChatAnalyticsFactory(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
  }

  @Override
  public ChatAnalytics get() {
    return provideChatAnalytics(firebaseAnalyticsProvider.get());
  }

  public static ChatModule_Companion_ProvideChatAnalyticsFactory create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    return new ChatModule_Companion_ProvideChatAnalyticsFactory(firebaseAnalyticsProvider);
  }

  public static ChatAnalytics provideChatAnalytics(FirebaseAnalytics firebaseAnalytics) {
    return Preconditions.checkNotNullFromProvides(ChatModule.Companion.provideChatAnalytics(firebaseAnalytics));
  }
}
