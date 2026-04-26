package br.com.brunocarvalhs.chat.commons.flags;

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
public final class ChatFeatureFlags_Factory implements Factory<ChatFeatureFlags> {
  private final Provider<FeatureFlagService> serviceProvider;

  private ChatFeatureFlags_Factory(Provider<FeatureFlagService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public ChatFeatureFlags get() {
    return newInstance(serviceProvider.get());
  }

  public static ChatFeatureFlags_Factory create(Provider<FeatureFlagService> serviceProvider) {
    return new ChatFeatureFlags_Factory(serviceProvider);
  }

  public static ChatFeatureFlags newInstance(FeatureFlagService service) {
    return new ChatFeatureFlags(service);
  }
}
