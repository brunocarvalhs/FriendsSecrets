package br.com.brunocarvalhs.group.details;

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
public final class GroupDetailsInitializerImpl_Factory implements Factory<GroupDetailsInitializerImpl> {
  private final Provider<CommonNavigator> navigatorProvider;

  private GroupDetailsInitializerImpl_Factory(Provider<CommonNavigator> navigatorProvider) {
    this.navigatorProvider = navigatorProvider;
  }

  @Override
  public GroupDetailsInitializerImpl get() {
    return newInstance(navigatorProvider.get());
  }

  public static GroupDetailsInitializerImpl_Factory create(
      Provider<CommonNavigator> navigatorProvider) {
    return new GroupDetailsInitializerImpl_Factory(navigatorProvider);
  }

  public static GroupDetailsInitializerImpl newInstance(CommonNavigator navigator) {
    return new GroupDetailsInitializerImpl(navigator);
  }
}
