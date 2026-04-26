package br.com.brunocarvalhs.group.create.app.domain.useCases;

import br.com.brunocarvalhs.group.create.app.domain.repositories.ContactsRepository;
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
public final class GetContactsUseCase_Factory implements Factory<GetContactsUseCase> {
  private final Provider<ContactsRepository> repositoryProvider;

  private GetContactsUseCase_Factory(Provider<ContactsRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetContactsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetContactsUseCase_Factory create(Provider<ContactsRepository> repositoryProvider) {
    return new GetContactsUseCase_Factory(repositoryProvider);
  }

  public static GetContactsUseCase newInstance(ContactsRepository repository) {
    return new GetContactsUseCase(repository);
  }
}
