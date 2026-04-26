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
public final class ClearMessagesUseCase_Factory implements Factory<ClearMessagesUseCase> {
  private final Provider<ChatRepository> repositoryProvider;

  private ClearMessagesUseCase_Factory(Provider<ChatRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ClearMessagesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ClearMessagesUseCase_Factory create(Provider<ChatRepository> repositoryProvider) {
    return new ClearMessagesUseCase_Factory(repositoryProvider);
  }

  public static ClearMessagesUseCase newInstance(ChatRepository repository) {
    return new ClearMessagesUseCase(repository);
  }
}
