package br.com.brunocarvalhs.group.details.app.presentation;

import androidx.lifecycle.SavedStateHandle;
import br.com.brunocarvalhs.group.details.app.domain.useCases.GroupDeleteUseCase;
import br.com.brunocarvalhs.group.details.app.domain.useCases.GroupExitUseCase;
import br.com.brunocarvalhs.group.details.app.domain.useCases.GroupReadUseCase;
import br.com.brunocarvalhs.group.details.app.domain.useCases.GroupShareUseCase;
import br.com.brunocarvalhs.group.details.commons.analytics.GroupDetailsAnalytics;
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
public final class GroupDetailsViewModel_Factory implements Factory<GroupDetailsViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GroupReadUseCase> readUseCaseProvider;

  private final Provider<GroupDeleteUseCase> deleteUseCaseProvider;

  private final Provider<GroupExitUseCase> exitUseCaseProvider;

  private final Provider<GroupShareUseCase> shareUseCaseProvider;

  private final Provider<GroupDetailsAnalytics> analyticsProvider;

  private GroupDetailsViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GroupReadUseCase> readUseCaseProvider,
      Provider<GroupDeleteUseCase> deleteUseCaseProvider,
      Provider<GroupExitUseCase> exitUseCaseProvider,
      Provider<GroupShareUseCase> shareUseCaseProvider,
      Provider<GroupDetailsAnalytics> analyticsProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.readUseCaseProvider = readUseCaseProvider;
    this.deleteUseCaseProvider = deleteUseCaseProvider;
    this.exitUseCaseProvider = exitUseCaseProvider;
    this.shareUseCaseProvider = shareUseCaseProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public GroupDetailsViewModel get() {
    return newInstance(savedStateHandleProvider.get(), readUseCaseProvider.get(), deleteUseCaseProvider.get(), exitUseCaseProvider.get(), shareUseCaseProvider.get(), analyticsProvider.get());
  }

  public static GroupDetailsViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GroupReadUseCase> readUseCaseProvider,
      Provider<GroupDeleteUseCase> deleteUseCaseProvider,
      Provider<GroupExitUseCase> exitUseCaseProvider,
      Provider<GroupShareUseCase> shareUseCaseProvider,
      Provider<GroupDetailsAnalytics> analyticsProvider) {
    return new GroupDetailsViewModel_Factory(savedStateHandleProvider, readUseCaseProvider, deleteUseCaseProvider, exitUseCaseProvider, shareUseCaseProvider, analyticsProvider);
  }

  public static GroupDetailsViewModel newInstance(SavedStateHandle savedStateHandle,
      GroupReadUseCase readUseCase, GroupDeleteUseCase deleteUseCase, GroupExitUseCase exitUseCase,
      GroupShareUseCase shareUseCase, GroupDetailsAnalytics analytics) {
    return new GroupDetailsViewModel(savedStateHandle, readUseCase, deleteUseCase, exitUseCase, shareUseCase, analytics);
  }
}
