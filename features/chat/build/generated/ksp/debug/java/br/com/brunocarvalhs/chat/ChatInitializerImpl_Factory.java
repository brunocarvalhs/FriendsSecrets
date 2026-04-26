package br.com.brunocarvalhs.chat;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ChatInitializerImpl_Factory implements Factory<ChatInitializerImpl> {
  @Override
  public ChatInitializerImpl get() {
    return newInstance();
  }

  public static ChatInitializerImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ChatInitializerImpl newInstance() {
    return new ChatInitializerImpl();
  }

  private static final class InstanceHolder {
    static final ChatInitializerImpl_Factory INSTANCE = new ChatInitializerImpl_Factory();
  }
}
