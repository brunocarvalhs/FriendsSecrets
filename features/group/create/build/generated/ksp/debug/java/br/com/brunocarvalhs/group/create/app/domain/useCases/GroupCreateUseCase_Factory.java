package br.com.brunocarvalhs.group.create.app.domain.useCases;

import br.com.brunocarvalhs.deviceid.DeviceService;
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository;
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
public final class GroupCreateUseCase_Factory implements Factory<GroupCreateUseCase> {
  private final Provider<GroupCreateRepository> repositoryProvider;

  private final Provider<StorageService> storageProvider;

  private final Provider<DeviceService> deviceServiceProvider;

  private GroupCreateUseCase_Factory(Provider<GroupCreateRepository> repositoryProvider,
      Provider<StorageService> storageProvider, Provider<DeviceService> deviceServiceProvider) {
    this.repositoryProvider = repositoryProvider;
    this.storageProvider = storageProvider;
    this.deviceServiceProvider = deviceServiceProvider;
  }

  @Override
  public GroupCreateUseCase get() {
    return newInstance(repositoryProvider.get(), storageProvider.get(), deviceServiceProvider.get());
  }

  public static GroupCreateUseCase_Factory create(
      Provider<GroupCreateRepository> repositoryProvider, Provider<StorageService> storageProvider,
      Provider<DeviceService> deviceServiceProvider) {
    return new GroupCreateUseCase_Factory(repositoryProvider, storageProvider, deviceServiceProvider);
  }

  public static GroupCreateUseCase newInstance(GroupCreateRepository repository,
      StorageService storage, DeviceService deviceService) {
    return new GroupCreateUseCase(repository, storage, deviceService);
  }
}
