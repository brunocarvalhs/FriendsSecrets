package br.com.brunocarvalhs.group.details.commons.flags;

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
public final class GroupDetailsFeatureFlags_Factory implements Factory<GroupDetailsFeatureFlags> {
  private final Provider<FeatureFlagService> serviceProvider;

  private GroupDetailsFeatureFlags_Factory(Provider<FeatureFlagService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public GroupDetailsFeatureFlags get() {
    return newInstance(serviceProvider.get());
  }

  public static GroupDetailsFeatureFlags_Factory create(
      Provider<FeatureFlagService> serviceProvider) {
    return new GroupDetailsFeatureFlags_Factory(serviceProvider);
  }

  public static GroupDetailsFeatureFlags newInstance(FeatureFlagService service) {
    return new GroupDetailsFeatureFlags(service);
  }
}
