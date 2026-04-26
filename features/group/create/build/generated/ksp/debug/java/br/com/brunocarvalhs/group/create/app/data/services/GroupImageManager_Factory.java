package br.com.brunocarvalhs.group.create.app.data.services;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
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
public final class GroupImageManager_Factory implements Factory<GroupImageManager> {
  private final Provider<FirebaseRemoteConfig> remoteConfigProvider;

  private GroupImageManager_Factory(Provider<FirebaseRemoteConfig> remoteConfigProvider) {
    this.remoteConfigProvider = remoteConfigProvider;
  }

  @Override
  public GroupImageManager get() {
    return newInstance(remoteConfigProvider.get());
  }

  public static GroupImageManager_Factory create(
      Provider<FirebaseRemoteConfig> remoteConfigProvider) {
    return new GroupImageManager_Factory(remoteConfigProvider);
  }

  public static GroupImageManager newInstance(FirebaseRemoteConfig remoteConfig) {
    return new GroupImageManager(remoteConfig);
  }
}
