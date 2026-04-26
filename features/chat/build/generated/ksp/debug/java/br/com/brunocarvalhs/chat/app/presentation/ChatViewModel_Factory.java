package br.com.brunocarvalhs.chat.app.presentation;

import androidx.lifecycle.SavedStateHandle;
import br.com.brunocarvalhs.chat.app.domain.usecase.ClearMessagesUseCase;
import br.com.brunocarvalhs.chat.app.domain.usecase.GetMessagesUseCase;
import br.com.brunocarvalhs.chat.app.domain.usecase.IdentifyUserUseCase;
import br.com.brunocarvalhs.chat.app.domain.usecase.SendMessageUseCase;
import br.com.brunocarvalhs.chat.commons.analytics.ChatAnalytics;
import br.com.brunocarvalhs.deviceid.DeviceService;
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
public final class ChatViewModel_Factory implements Factory<ChatViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetMessagesUseCase> getMessagesUseCaseProvider;

  private final Provider<SendMessageUseCase> sendMessageUseCaseProvider;

  private final Provider<ClearMessagesUseCase> clearMessagesUseCaseProvider;

  private final Provider<IdentifyUserUseCase> identifyUserUseCaseProvider;

  private final Provider<DeviceService> deviceServiceProvider;

  private final Provider<ChatAnalytics> analyticsProvider;

  private ChatViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetMessagesUseCase> getMessagesUseCaseProvider,
      Provider<SendMessageUseCase> sendMessageUseCaseProvider,
      Provider<ClearMessagesUseCase> clearMessagesUseCaseProvider,
      Provider<IdentifyUserUseCase> identifyUserUseCaseProvider,
      Provider<DeviceService> deviceServiceProvider, Provider<ChatAnalytics> analyticsProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getMessagesUseCaseProvider = getMessagesUseCaseProvider;
    this.sendMessageUseCaseProvider = sendMessageUseCaseProvider;
    this.clearMessagesUseCaseProvider = clearMessagesUseCaseProvider;
    this.identifyUserUseCaseProvider = identifyUserUseCaseProvider;
    this.deviceServiceProvider = deviceServiceProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public ChatViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getMessagesUseCaseProvider.get(), sendMessageUseCaseProvider.get(), clearMessagesUseCaseProvider.get(), identifyUserUseCaseProvider.get(), deviceServiceProvider.get(), analyticsProvider.get());
  }

  public static ChatViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetMessagesUseCase> getMessagesUseCaseProvider,
      Provider<SendMessageUseCase> sendMessageUseCaseProvider,
      Provider<ClearMessagesUseCase> clearMessagesUseCaseProvider,
      Provider<IdentifyUserUseCase> identifyUserUseCaseProvider,
      Provider<DeviceService> deviceServiceProvider, Provider<ChatAnalytics> analyticsProvider) {
    return new ChatViewModel_Factory(savedStateHandleProvider, getMessagesUseCaseProvider, sendMessageUseCaseProvider, clearMessagesUseCaseProvider, identifyUserUseCaseProvider, deviceServiceProvider, analyticsProvider);
  }

  public static ChatViewModel newInstance(SavedStateHandle savedStateHandle,
      GetMessagesUseCase getMessagesUseCase, SendMessageUseCase sendMessageUseCase,
      ClearMessagesUseCase clearMessagesUseCase, IdentifyUserUseCase identifyUserUseCase,
      DeviceService deviceService, ChatAnalytics analytics) {
    return new ChatViewModel(savedStateHandle, getMessagesUseCase, sendMessageUseCase, clearMessagesUseCase, identifyUserUseCase, deviceService, analytics);
  }
}
