package br.com.brunocarvalhs.group.create;

import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator;
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
public final class GroupCreateInitializerImpl_Factory implements Factory<GroupCreateInitializerImpl> {
  private final Provider<CommonNavigator> navigatorProvider;

  private GroupCreateInitializerImpl_Factory(Provider<CommonNavigator> navigatorProvider) {
    this.navigatorProvider = navigatorProvider;
  }

  @Override
  public GroupCreateInitializerImpl get() {
    return newInstance(navigatorProvider.get());
  }

  public static GroupCreateInitializerImpl_Factory create(
      Provider<CommonNavigator> navigatorProvider) {
    return new GroupCreateInitializerImpl_Factory(navigatorProvider);
  }

  public static GroupCreateInitializerImpl newInstance(CommonNavigator navigator) {
    return new GroupCreateInitializerImpl(navigator);
  }
}
