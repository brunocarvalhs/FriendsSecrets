package br.com.brunocarvalhs.group.list.commons.di

import br.com.brunocarvalhs.group.list.app.data.repository.GroupListRepositoryImpl
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.group.list.app.presentation.GroupListAnalytics
import br.com.brunocarvalhs.group.list.app.presentation.GroupListAnalyticsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class GroupListModule {
    @Binds
    internal abstract fun bindGroupListRepository(
        impl: GroupListRepositoryImpl
    ): GroupListRepository

    @Binds
    internal abstract fun bindGroupListAnalytics(
        impl: GroupListAnalyticsImpl
    ): GroupListAnalytics
}
