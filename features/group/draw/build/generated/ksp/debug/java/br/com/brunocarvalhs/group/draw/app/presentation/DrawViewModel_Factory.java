package br.com.brunocarvalhs.group.draw.app.presentation;

import androidx.lifecycle.SavedStateHandle;
import br.com.brunocarvalhs.group.draw.app.domain.useCases.DrawUseCase;
import br.com.brunocarvalhs.group.draw.app.domain.useCases.ShareSecretFriendsUseCase;
import br.com.brunocarvalhs.group.draw.commons.analytics.DrawAnalytics;
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
public final class DrawViewModel_Factory implements Factory<DrawViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<ShareSecretFriendsUseCase> shareSecretFriendsUseCaseProvider;

  private final Provider<DrawUseCase> drawUseCaseProvider;

  private final Provider<DrawAnalytics> analyticsProvider;

  private DrawViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<ShareSecretFriendsUseCase> shareSecretFriendsUseCaseProvider,
      Provider<DrawUseCase> drawUseCaseProvider, Provider<DrawAnalytics> analyticsProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.shareSecretFriendsUseCaseProvider = shareSecretFriendsUseCaseProvider;
    this.drawUseCaseProvider = drawUseCaseProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public DrawViewModel get() {
    return newInstance(savedStateHandleProvider.get(), shareSecretFriendsUseCaseProvider.get(), drawUseCaseProvider.get(), analyticsProvider.get());
  }

  public static DrawViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<ShareSecretFriendsUseCase> shareSecretFriendsUseCaseProvider,
      Provider<DrawUseCase> drawUseCaseProvider, Provider<DrawAnalytics> analyticsProvider) {
    return new DrawViewModel_Factory(savedStateHandleProvider, shareSecretFriendsUseCaseProvider, drawUseCaseProvider, analyticsProvider);
  }

  public static DrawViewModel newInstance(SavedStateHandle savedStateHandle,
      ShareSecretFriendsUseCase shareSecretFriendsUseCase, DrawUseCase drawUseCase,
      DrawAnalytics analytics) {
    return new DrawViewModel(savedStateHandle, shareSecretFriendsUseCase, drawUseCase, analytics);
  }
}
