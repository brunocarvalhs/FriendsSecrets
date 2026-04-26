package br.com.brunocarvalhs.group.list.commons.di

import br.com.brunocarvalhs.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.list.GroupListInitializerImpl
import br.com.brunocarvalhs.group.list.app.data.repository.GroupListRepositoryImpl
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.group.list.commons.analytics.GroupListAnalytics
import br.com.brunocarvalhs.group.list.commons.analytics.GroupListAnalyticsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal abstract class GroupListModule {

    @Binds
    @IntoSet
    abstract fun bindGroupListInitializer(
        impl: GroupListInitializerImpl
    ): FeatureInitializer

    @Binds
    internal abstract fun bindGroupListRepository(
        impl: GroupListRepositoryImpl
    ): GroupListRepository

    @Binds
    internal abstract fun bindGroupListAnalytics(
        impl: GroupListAnalyticsImpl
    ): GroupListAnalytics
}
