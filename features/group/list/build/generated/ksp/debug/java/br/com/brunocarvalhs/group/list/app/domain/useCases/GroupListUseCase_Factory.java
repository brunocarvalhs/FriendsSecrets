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
public final class GroupListUseCase_Factory implements Factory<GroupListUseCase> {
  private final Provider<GroupListRepository> repositoryProvider;

  private final Provider<StorageService> storageProvider;

  private final Provider<DeviceService> deviceServiceProvider;

  private GroupListUseCase_Factory(Provider<GroupListRepository> repositoryProvider,
      Provider<StorageService> storageProvider, Provider<DeviceService> deviceServiceProvider) {
    this.repositoryProvider = repositoryProvider;
    this.storageProvider = storageProvider;
    this.deviceServiceProvider = deviceServiceProvider;
  }

  @Override
  public GroupListUseCase get() {
    return newInstance(repositoryProvider.get(), storageProvider.get(), deviceServiceProvider.get());
  }

  public static GroupListUseCase_Factory create(Provider<GroupListRepository> repositoryProvider,
      Provider<StorageService> storageProvider, Provider<DeviceService> deviceServiceProvider) {
    return new GroupListUseCase_Factory(repositoryProvider, storageProvider, deviceServiceProvider);
  }

  public static GroupListUseCase newInstance(GroupListRepository repository, StorageService storage,
      DeviceService deviceService) {
    return new GroupListUseCase(repository, storage, deviceService);
  }
}
