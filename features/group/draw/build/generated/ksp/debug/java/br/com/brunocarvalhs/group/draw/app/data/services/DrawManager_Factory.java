package br.com.brunocarvalhs.group.draw.app.data.services;

import br.com.brunocarvalhs.friendssecrets.core.security.domain.CryptoService;
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
public final class DrawManager_Factory implements Factory<DrawManager> {
  private final Provider<CryptoService> cryptoProvider;

  private DrawManager_Factory(Provider<CryptoService> cryptoProvider) {
    this.cryptoProvider = cryptoProvider;
  }

  @Override
  public DrawManager get() {
    return newInstance(cryptoProvider.get());
  }

  public static DrawManager_Factory create(Provider<CryptoService> cryptoProvider) {
    return new DrawManager_Factory(cryptoProvider);
  }

  public static DrawManager newInstance(CryptoService crypto) {
    return new DrawManager(crypto);
  }
}
