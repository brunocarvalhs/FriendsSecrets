package br.com.brunocarvalhs.group.draw.commons.flags;

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
public final class GroupDrawFeatureFlags_Factory implements Factory<GroupDrawFeatureFlags> {
  private final Provider<FeatureFlagService> serviceProvider;

  private GroupDrawFeatureFlags_Factory(Provider<FeatureFlagService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public GroupDrawFeatureFlags get() {
    return newInstance(serviceProvider.get());
  }

  public static GroupDrawFeatureFlags_Factory create(Provider<FeatureFlagService> serviceProvider) {
    return new GroupDrawFeatureFlags_Factory(serviceProvider);
  }

  public static GroupDrawFeatureFlags newInstance(FeatureFlagService service) {
    return new GroupDrawFeatureFlags(service);
  }
}
