package br.com.brunocarvalhs.group.create.app.presentation.contacts;

import androidx.lifecycle.SavedStateHandle;
import br.com.brunocarvalhs.group.create.app.domain.useCases.GetContactsUseCase;
import br.com.brunocarvalhs.group.create.commons.analytics.GroupCreateAnalytics;
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
public final class ContactsViewModel_Factory implements Factory<ContactsViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetContactsUseCase> getContactsUseCaseProvider;

  private final Provider<GroupCreateAnalytics> analyticsProvider;

  private ContactsViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetContactsUseCase> getContactsUseCaseProvider,
      Provider<GroupCreateAnalytics> analyticsProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getContactsUseCaseProvider = getContactsUseCaseProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public ContactsViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getContactsUseCaseProvider.get(), analyticsProvider.get());
  }

  public static ContactsViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetContactsUseCase> getContactsUseCaseProvider,
      Provider<GroupCreateAnalytics> analyticsProvider) {
    return new ContactsViewModel_Factory(savedStateHandleProvider, getContactsUseCaseProvider, analyticsProvider);
  }

  public static ContactsViewModel newInstance(SavedStateHandle savedStateHandle,
      GetContactsUseCase getContactsUseCase, GroupCreateAnalytics analytics) {
    return new ContactsViewModel(savedStateHandle, getContactsUseCase, analytics);
  }
}
