package br.com.brunocarvalhs.group.draw.app.domain.useCases;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ShareSecretFriendsUseCase_Factory implements Factory<ShareSecretFriendsUseCase> {
  private final Provider<Context> contextProvider;

  private ShareSecretFriendsUseCase_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ShareSecretFriendsUseCase get() {
    return newInstance(contextProvider.get());
  }

  public static ShareSecretFriendsUseCase_Factory create(Provider<Context> contextProvider) {
    return new ShareSecretFriendsUseCase_Factory(contextProvider);
  }

  public static ShareSecretFriendsUseCase newInstance(Context context) {
    return new ShareSecretFriendsUseCase(context);
  }
}
