package br.com.brunocarvalhs.chat.app.domain.usecase;

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository;
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
public final class SendMessageUseCase_Factory implements Factory<SendMessageUseCase> {
  private final Provider<ChatRepository> repositoryProvider;

  private SendMessageUseCase_Factory(Provider<ChatRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SendMessageUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SendMessageUseCase_Factory create(Provider<ChatRepository> repositoryProvider) {
    return new SendMessageUseCase_Factory(repositoryProvider);
  }

  public static SendMessageUseCase newInstance(ChatRepository repository) {
    return new SendMessageUseCase(repository);
  }
}
