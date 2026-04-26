package br.com.brunocarvalhs.group.create.app.data.repository;

import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService;
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
public final class GroupCreateRepositoryImpl_Factory implements Factory<GroupCreateRepositoryImpl> {
  private final Provider<NetworkService> networkProvider;

  private GroupCreateRepositoryImpl_Factory(Provider<NetworkService> networkProvider) {
    this.networkProvider = networkProvider;
  }

  @Override
  public GroupCreateRepositoryImpl get() {
    return newInstance(networkProvider.get());
  }

  public static GroupCreateRepositoryImpl_Factory create(Provider<NetworkService> networkProvider) {
    return new GroupCreateRepositoryImpl_Factory(networkProvider);
  }

  public static GroupCreateRepositoryImpl newInstance(NetworkService network) {
    return new GroupCreateRepositoryImpl(network);
  }
}
