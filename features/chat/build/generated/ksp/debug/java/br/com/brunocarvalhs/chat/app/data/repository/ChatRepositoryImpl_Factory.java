package br.com.brunocarvalhs.chat.app.data.repository;

import br.com.brunocarvalhs.chat.app.domain.services.ChatService;
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
public final class ChatRepositoryImpl_Factory implements Factory<ChatRepositoryImpl> {
  private final Provider<ChatService> chatServiceProvider;

  private ChatRepositoryImpl_Factory(Provider<ChatService> chatServiceProvider) {
    this.chatServiceProvider = chatServiceProvider;
  }

  @Override
  public ChatRepositoryImpl get() {
    return newInstance(chatServiceProvider.get());
  }

  public static ChatRepositoryImpl_Factory create(Provider<ChatService> chatServiceProvider) {
    return new ChatRepositoryImpl_Factory(chatServiceProvider);
  }

  public static ChatRepositoryImpl newInstance(ChatService chatService) {
    return new ChatRepositoryImpl(chatService);
  }
}
