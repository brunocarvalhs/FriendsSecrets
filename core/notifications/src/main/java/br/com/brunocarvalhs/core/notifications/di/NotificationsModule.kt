package br.com.brunocarvalhs.core.notifications.di

import br.com.brunocarvalhs.core.notifications.data.GroupSyncSchedulerImpl
import br.com.brunocarvalhs.core.notifications.domain.GroupSyncScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NotificationsModule {

    @Binds
    internal abstract fun bindGroupSyncScheduler(
        impl: GroupSyncSchedulerImpl
    ): GroupSyncScheduler
}
