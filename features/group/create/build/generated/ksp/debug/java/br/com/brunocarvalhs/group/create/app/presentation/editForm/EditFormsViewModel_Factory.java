package br.com.brunocarvalhs.group.create.app.presentation.editForm;

import androidx.lifecycle.SavedStateHandle;
import br.com.brunocarvalhs.group.create.app.domain.services.GroupImageService;
import br.com.brunocarvalhs.group.create.app.domain.useCases.GroupEditUseCase;
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
public final class EditFormsViewModel_Factory implements Factory<EditFormsViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GroupEditUseCase> groupEditUseCaseProvider;

  private final Provider<GroupImageService> imageServiceProvider;

  private final Provider<GroupCreateAnalytics> analyticsProvider;

  private EditFormsViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GroupEditUseCase> groupEditUseCaseProvider,
      Provider<GroupImageService> imageServiceProvider,
      Provider<GroupCreateAnalytics> analyticsProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.groupEditUseCaseProvider = groupEditUseCaseProvider;
    this.imageServiceProvider = imageServiceProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public EditFormsViewModel get() {
    return newInstance(savedStateHandleProvider.get(), groupEditUseCaseProvider.get(), imageServiceProvider.get(), analyticsProvider.get());
  }

  public static EditFormsViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GroupEditUseCase> groupEditUseCaseProvider,
      Provider<GroupImageService> imageServiceProvider,
      Provider<GroupCreateAnalytics> analyticsProvider) {
    return new EditFormsViewModel_Factory(savedStateHandleProvider, groupEditUseCaseProvider, imageServiceProvider, analyticsProvider);
  }

  public static EditFormsViewModel newInstance(SavedStateHandle savedStateHandle,
      GroupEditUseCase groupEditUseCase, GroupImageService imageService,
      GroupCreateAnalytics analytics) {
    return new EditFormsViewModel(savedStateHandle, groupEditUseCase, imageService, analytics);
  }
}
