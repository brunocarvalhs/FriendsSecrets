package br.com.brunocarvalhs.group.create.app.domain.useCases;

import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository;
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
public final class GroupEditUseCase_Factory implements Factory<GroupEditUseCase> {
  private final Provider<GroupCreateRepository> repositoryProvider;

  private GroupEditUseCase_Factory(Provider<GroupCreateRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GroupEditUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GroupEditUseCase_Factory create(
      Provider<GroupCreateRepository> repositoryProvider) {
    return new GroupEditUseCase_Factory(repositoryProvider);
  }

  public static GroupEditUseCase newInstance(GroupCreateRepository repository) {
    return new GroupEditUseCase(repository);
  }
}
