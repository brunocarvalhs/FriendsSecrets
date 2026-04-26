package br.com.brunocarvalhs.group.list.app.data.repository;

import br.com.brunocarvalhs.deviceid.DeviceService;
import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService;
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
public final class GroupListRepositoryImpl_Factory implements Factory<GroupListRepositoryImpl> {
  private final Provider<NetworkService> networkProvider;

  private final Provider<DeviceService> deviceProvider;

  private GroupListRepositoryImpl_Factory(Provider<NetworkService> networkProvider,
      Provider<DeviceService> deviceProvider) {
    this.networkProvider = networkProvider;
    this.deviceProvider = deviceProvider;
  }

  @Override
  public GroupListRepositoryImpl get() {
    return newInstance(networkProvider.get(), deviceProvider.get());
  }

  public static GroupListRepositoryImpl_Factory create(Provider<NetworkService> networkProvider,
      Provider<DeviceService> deviceProvider) {
    return new GroupListRepositoryImpl_Factory(networkProvider, deviceProvider);
  }

  public static GroupListRepositoryImpl newInstance(NetworkService network, DeviceService device) {
    return new GroupListRepositoryImpl(network, device);
  }
}
