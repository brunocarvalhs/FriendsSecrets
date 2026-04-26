package br.com.brunocarvalhs.group.details.app.data.repository;

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
public final class GroupDetailsRepositoryImpl_Factory implements Factory<GroupDetailsRepositoryImpl> {
  private final Provider<NetworkService> networkProvider;

  private GroupDetailsRepositoryImpl_Factory(Provider<NetworkService> networkProvider) {
    this.networkProvider = networkProvider;
  }

  @Override
  public GroupDetailsRepositoryImpl get() {
    return newInstance(networkProvider.get());
  }

  public static GroupDetailsRepositoryImpl_Factory create(
      Provider<NetworkService> networkProvider) {
    return new GroupDetailsRepositoryImpl_Factory(networkProvider);
  }

  public static GroupDetailsRepositoryImpl newInstance(NetworkService network) {
    return new GroupDetailsRepositoryImpl(network);
  }
}
