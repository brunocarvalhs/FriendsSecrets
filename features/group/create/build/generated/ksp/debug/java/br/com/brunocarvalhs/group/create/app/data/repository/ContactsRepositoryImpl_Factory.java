package br.com.brunocarvalhs.group.create.app.data.repository;

import br.com.brunocarvalhs.group.create.app.domain.services.ContactService;
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
public final class ContactsRepositoryImpl_Factory implements Factory<ContactsRepositoryImpl> {
  private final Provider<ContactService> serviceProvider;

  private ContactsRepositoryImpl_Factory(Provider<ContactService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public ContactsRepositoryImpl get() {
    return newInstance(serviceProvider.get());
  }

  public static ContactsRepositoryImpl_Factory create(Provider<ContactService> serviceProvider) {
    return new ContactsRepositoryImpl_Factory(serviceProvider);
  }

  public static ContactsRepositoryImpl newInstance(ContactService service) {
    return new ContactsRepositoryImpl(service);
  }
}
