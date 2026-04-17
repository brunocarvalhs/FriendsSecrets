package br.com.brunocarvalhs.group.details.commons.di

import br.com.brunocarvalhs.group.details.app.data.repository.GroupDetailsRepositoryImpl
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import br.com.brunocarvalhs.group.details.commons.analytics.GroupDetailsAnalytics
import br.com.brunocarvalhs.group.details.commons.analytics.GroupDetailsAnalyticsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class GroupDetailsModule {
    @Binds
    internal abstract fun bindGroupListRepository(
        impl: GroupDetailsRepositoryImpl
    ): GroupDetailsRepository

    @Binds
    internal abstract fun bindGroupDetailsAnalytics(
        impl: GroupDetailsAnalyticsImpl
    ): GroupDetailsAnalytics
}
