package br.com.brunocarvalhs.group.create.commons.di;

import br.com.brunocarvalhs.group.create.commons.analytics.GroupCreateAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class GroupCreateModule_Companion_ProvideGroupCreateAnalyticsFactory implements Factory<GroupCreateAnalytics> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  private GroupCreateModule_Companion_ProvideGroupCreateAnalyticsFactory(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
  }

  @Override
  public GroupCreateAnalytics get() {
    return provideGroupCreateAnalytics(firebaseAnalyticsProvider.get());
  }

  public static GroupCreateModule_Companion_ProvideGroupCreateAnalyticsFactory create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    return new GroupCreateModule_Companion_ProvideGroupCreateAnalyticsFactory(firebaseAnalyticsProvider);
  }

  public static GroupCreateAnalytics provideGroupCreateAnalytics(
      FirebaseAnalytics firebaseAnalytics) {
    return Preconditions.checkNotNullFromProvides(GroupCreateModule.Companion.provideGroupCreateAnalytics(firebaseAnalytics));
  }
}
