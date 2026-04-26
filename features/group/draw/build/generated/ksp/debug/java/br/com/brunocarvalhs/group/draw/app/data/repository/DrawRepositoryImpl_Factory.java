package br.com.brunocarvalhs.group.draw.app.data.repository;

import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService;
import br.com.brunocarvalhs.group.draw.app.data.services.DrawManager;
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
public final class DrawRepositoryImpl_Factory implements Factory<DrawRepositoryImpl> {
  private final Provider<NetworkService> networkProvider;

  private final Provider<DrawManager> drawServiceProvider;

  private DrawRepositoryImpl_Factory(Provider<NetworkService> networkProvider,
      Provider<DrawManager> drawServiceProvider) {
    this.networkProvider = networkProvider;
    this.drawServiceProvider = drawServiceProvider;
  }

  @Override
  public DrawRepositoryImpl get() {
    return newInstance(networkProvider.get(), drawServiceProvider.get());
  }

  public static DrawRepositoryImpl_Factory create(Provider<NetworkService> networkProvider,
      Provider<DrawManager> drawServiceProvider) {
    return new DrawRepositoryImpl_Factory(networkProvider, drawServiceProvider);
  }

  public static DrawRepositoryImpl newInstance(NetworkService network, DrawManager drawService) {
    return new DrawRepositoryImpl(network, drawService);
  }
}
