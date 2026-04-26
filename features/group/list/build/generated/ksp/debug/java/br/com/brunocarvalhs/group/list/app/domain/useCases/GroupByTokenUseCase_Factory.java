package br.com.brunocarvalhs.group.list.app.domain.useCases;

import br.com.brunocarvalhs.deviceid.DeviceService;
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository;
import br.com.brunocarvalhs.storage.domain.StorageService;
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
public final class GroupByTokenUseCase_Factory implements Factory<GroupByTokenUseCase> {
  private final Provider<GroupListRepository> repositoryProvider;

  private final Provider<StorageService> storageProvider;

  private final Provider<DeviceService> deviceProvider;

  private GroupByTokenUseCase_Factory(Provider<GroupListRepository> repositoryProvider,
      Provider<StorageService> storageProvider, Provider<DeviceService> deviceProvider) {
    this.repositoryProvider = repositoryProvider;
    this.storageProvider = storageProvider;
    this.deviceProvider = deviceProvider;
  }

  @Override
  public GroupByTokenUseCase get() {
    return newInstance(repositoryProvider.get(), storageProvider.get(), deviceProvider.get());
  }

  public static GroupByTokenUseCase_Factory create(Provider<GroupListRepository> repositoryProvider,
      Provider<StorageService> storageProvider, Provider<DeviceService> deviceProvider) {
    return new GroupByTokenUseCase_Factory(repositoryProvider, storageProvider, deviceProvider);
  }

  public static GroupByTokenUseCase newInstance(GroupListRepository repository,
      StorageService storage, DeviceService device) {
    return new GroupByTokenUseCase(repository, storage, device);
  }
}
