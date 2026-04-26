package br.com.brunocarvalhs.group.draw.app.domain.useCases;

import br.com.brunocarvalhs.group.draw.app.domain.repository.DrawRepository;
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
public final class DrawUseCase_Factory implements Factory<DrawUseCase> {
  private final Provider<DrawRepository> repositoryProvider;

  private DrawUseCase_Factory(Provider<DrawRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DrawUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static DrawUseCase_Factory create(Provider<DrawRepository> repositoryProvider) {
    return new DrawUseCase_Factory(repositoryProvider);
  }

  public static DrawUseCase newInstance(DrawRepository repository) {
    return new DrawUseCase(repository);
  }
}
