package br.com.brunocarvalhs.group.list;

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
public final class GroupListInitializerImpl_Factory implements Factory<GroupListInitializerImpl> {
  private final Provider<CommonNavigator> navigatorProvider;

  private GroupListInitializerImpl_Factory(Provider<CommonNavigator> navigatorProvider) {
    this.navigatorProvider = navigatorProvider;
  }

  @Override
  public GroupListInitializerImpl get() {
    return newInstance(navigatorProvider.get());
  }

  public static GroupListInitializerImpl_Factory create(
      Provider<CommonNavigator> navigatorProvider) {
    return new GroupListInitializerImpl_Factory(navigatorProvider);
  }

  public static GroupListInitializerImpl newInstance(CommonNavigator navigator) {
    return new GroupListInitializerImpl(navigator);
  }
}
