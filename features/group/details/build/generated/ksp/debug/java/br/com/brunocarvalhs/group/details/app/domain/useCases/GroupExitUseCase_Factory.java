package br.com.brunocarvalhs.group.details.app.domain.useCases;

import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository;
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
public final class GroupExitUseCase_Factory implements Factory<GroupExitUseCase> {
  private final Provider<GroupDetailsRepository> repositoryProvider;

  private final Provider<StorageService> storageProvider;

  private GroupExitUseCase_Factory(Provider<GroupDetailsRepository> repositoryProvider,
      Provider<StorageService> storageProvider) {
    this.repositoryProvider = repositoryProvider;
    this.storageProvider = storageProvider;
  }

  @Override
  public GroupExitUseCase get() {
    return newInstance(repositoryProvider.get(), storageProvider.get());
  }

  public static GroupExitUseCase_Factory create(Provider<GroupDetailsRepository> repositoryProvider,
      Provider<StorageService> storageProvider) {
    return new GroupExitUseCase_Factory(repositoryProvider, storageProvider);
  }

  public static GroupExitUseCase newInstance(GroupDetailsRepository repository,
      StorageService storage) {
    return new GroupExitUseCase(repository, storage);
  }
}
