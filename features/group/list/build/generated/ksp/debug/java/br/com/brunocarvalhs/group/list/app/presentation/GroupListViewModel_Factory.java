package br.com.brunocarvalhs.group.list.app.presentation;

import br.com.brunocarvalhs.group.list.app.domain.useCases.GroupByTokenUseCase;
import br.com.brunocarvalhs.group.list.app.domain.useCases.GroupListUseCase;
import br.com.brunocarvalhs.group.list.commons.analytics.GroupListAnalytics;
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
public final class GroupListViewModel_Factory implements Factory<GroupListViewModel> {
  private final Provider<GroupListUseCase> groupListUseCaseProvider;

  private final Provider<GroupByTokenUseCase> groupByTokenUseCaseProvider;

  private final Provider<GroupListAnalytics> analyticsProvider;

  private GroupListViewModel_Factory(Provider<GroupListUseCase> groupListUseCaseProvider,
      Provider<GroupByTokenUseCase> groupByTokenUseCaseProvider,
      Provider<GroupListAnalytics> analyticsProvider) {
    this.groupListUseCaseProvider = groupListUseCaseProvider;
    this.groupByTokenUseCaseProvider = groupByTokenUseCaseProvider;
    this.analyticsProvider = analyticsProvider;
  }

  @Override
  public GroupListViewModel get() {
    return newInstance(groupListUseCaseProvider.get(), groupByTokenUseCaseProvider.get(), analyticsProvider.get());
  }

  public static GroupListViewModel_Factory create(
      Provider<GroupListUseCase> groupListUseCaseProvider,
      Provider<GroupByTokenUseCase> groupByTokenUseCaseProvider,
      Provider<GroupListAnalytics> analyticsProvider) {
    return new GroupListViewModel_Factory(groupListUseCaseProvider, groupByTokenUseCaseProvider, analyticsProvider);
  }

  public static GroupListViewModel newInstance(GroupListUseCase groupListUseCase,
      GroupByTokenUseCase groupByTokenUseCase, GroupListAnalytics analytics) {
    return new GroupListViewModel(groupListUseCase, groupByTokenUseCase, analytics);
  }
}
