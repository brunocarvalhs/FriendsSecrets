package br.com.brunocarvalhs.group.list.commons.flags;

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
public final class GroupListFeatureFlags_Factory implements Factory<GroupListFeatureFlags> {
  private final Provider<FeatureFlagService> serviceProvider;

  private GroupListFeatureFlags_Factory(Provider<FeatureFlagService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public GroupListFeatureFlags get() {
    return newInstance(serviceProvider.get());
  }

  public static GroupListFeatureFlags_Factory create(Provider<FeatureFlagService> serviceProvider) {
    return new GroupListFeatureFlags_Factory(serviceProvider);
  }

  public static GroupListFeatureFlags newInstance(FeatureFlagService service) {
    return new GroupListFeatureFlags(service);
  }
}
