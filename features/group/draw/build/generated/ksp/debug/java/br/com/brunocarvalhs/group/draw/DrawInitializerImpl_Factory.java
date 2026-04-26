package br.com.brunocarvalhs.group.draw;

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
public final class DrawInitializerImpl_Factory implements Factory<DrawInitializerImpl> {
  @Override
  public DrawInitializerImpl get() {
    return newInstance();
  }

  public static DrawInitializerImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DrawInitializerImpl newInstance() {
    return new DrawInitializerImpl();
  }

  private static final class InstanceHolder {
    static final DrawInitializerImpl_Factory INSTANCE = new DrawInitializerImpl_Factory();
  }
}
