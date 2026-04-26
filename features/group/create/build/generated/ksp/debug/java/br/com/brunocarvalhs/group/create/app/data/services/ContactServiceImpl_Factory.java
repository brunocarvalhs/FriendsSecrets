package br.com.brunocarvalhs.group.create.app.data.services;

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
public final class ContactServiceImpl_Factory implements Factory<ContactServiceImpl> {
  private final Provider<Context> contextProvider;

  private ContactServiceImpl_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ContactServiceImpl get() {
    return newInstance(contextProvider.get());
  }

  public static ContactServiceImpl_Factory create(Provider<Context> contextProvider) {
    return new ContactServiceImpl_Factory(contextProvider);
  }

  public static ContactServiceImpl newInstance(Context context) {
    return new ContactServiceImpl(context);
  }
}
