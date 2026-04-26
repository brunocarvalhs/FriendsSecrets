package br.com.brunocarvalhs.group.create.app.presentation.forms;

import androidx.lifecycle.SavedStateHandle;
import br.com.brunocarvalhs.group.create.app.domain.services.GroupImageService;
import br.com.brunocarvalhs.group.create.app.domain.useCases.GroupCreateUseCase;
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
public final class FormsViewModel_Factory implements Factory<FormsViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GroupCreateUseCase> groupCreateUseCaseProvider;

  private final Provider<GroupImageService> imageServiceProvider;

  private final Provider<GroupCreateAnalytics> analyticsProvider;

  private FormsViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GroupCreateUseCase> groupCreateUseCaseProvider,
      Provider<GroupImageService> imageServiceProvider,
      Provider<GroupCreateAnalytics> analyticsProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.groupCreateUseCaseProvider = groupCreateUseCaseProvider;
    this.imageServiceProvider = imageServiceProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public FormsViewModel get() {
    return newInstance(savedStateHandleProvider.get(), groupCreateUseCaseProvider.get(), imageServiceProvider.get(), analyticsProvider.get());
  }

  public static FormsViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GroupCreateUseCase> groupCreateUseCaseProvider,
      Provider<GroupImageService> imageServiceProvider,
      Provider<GroupCreateAnalytics> analyticsProvider) {
    return new FormsViewModel_Factory(savedStateHandleProvider, groupCreateUseCaseProvider, imageServiceProvider, analyticsProvider);
  }

  public static FormsViewModel newInstance(SavedStateHandle savedStateHandle,
      GroupCreateUseCase groupCreateUseCase, GroupImageService imageService,
      GroupCreateAnalytics analytics) {
    return new FormsViewModel(savedStateHandle, groupCreateUseCase, imageService, analytics);
  }
}
