package br.com.brunocarvalhs.group.details.app.domain.useCases;

import br.com.brunocarvalhs.deviceid.DeviceService;
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository;
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
public final class GroupReadUseCase_Factory implements Factory<GroupReadUseCase> {
  private final Provider<GroupDetailsRepository> repositoryProvider;

  private final Provider<DeviceService> deviceServiceProvider;

  private GroupReadUseCase_Factory(Provider<GroupDetailsRepository> repositoryProvider,
      Provider<DeviceService> deviceServiceProvider) {
    this.repositoryProvider = repositoryProvider;
    this.deviceServiceProvider = deviceServiceProvider;
  }

  @Override
  public GroupReadUseCase get() {
    return newInstance(repositoryProvider.get(), deviceServiceProvider.get());
  }

  public static GroupReadUseCase_Factory create(Provider<GroupDetailsRepository> repositoryProvider,
      Provider<DeviceService> deviceServiceProvider) {
    return new GroupReadUseCase_Factory(repositoryProvider, deviceServiceProvider);
  }

  public static GroupReadUseCase newInstance(GroupDetailsRepository repository,
      DeviceService deviceService) {
    return new GroupReadUseCase(repository, deviceService);
  }
}
