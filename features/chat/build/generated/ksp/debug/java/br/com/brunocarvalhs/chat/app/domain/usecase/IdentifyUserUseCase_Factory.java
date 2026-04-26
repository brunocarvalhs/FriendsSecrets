package br.com.brunocarvalhs.chat.app.domain.usecase;

import br.com.brunocarvalhs.storage.domain.StorageService;
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
public final class IdentifyUserUseCase_Factory implements Factory<IdentifyUserUseCase> {
  private final Provider<StorageService> storageServiceProvider;

  private IdentifyUserUseCase_Factory(Provider<StorageService> storageServiceProvider) {
    this.storageServiceProvider = storageServiceProvider;
  }

  @Override
  public IdentifyUserUseCase get() {
    return newInstance(storageServiceProvider.get());
  }

  public static IdentifyUserUseCase_Factory create(
      Provider<StorageService> storageServiceProvider) {
    return new IdentifyUserUseCase_Factory(storageServiceProvider);
  }

  public static IdentifyUserUseCase newInstance(StorageService storageService) {
    return new IdentifyUserUseCase(storageService);
  }
}
