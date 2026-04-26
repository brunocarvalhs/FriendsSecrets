package br.com.brunocarvalhs.group.create.commons.flags;

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
public final class GroupCreateFeatureFlags_Factory implements Factory<GroupCreateFeatureFlags> {
  private final Provider<FeatureFlagService> serviceProvider;

  private GroupCreateFeatureFlags_Factory(Provider<FeatureFlagService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public GroupCreateFeatureFlags get() {
    return newInstance(serviceProvider.get());
  }

  public static GroupCreateFeatureFlags_Factory create(
      Provider<FeatureFlagService> serviceProvider) {
    return new GroupCreateFeatureFlags_Factory(serviceProvider);
  }

  public static GroupCreateFeatureFlags newInstance(FeatureFlagService service) {
    return new GroupCreateFeatureFlags(service);
  }
}
