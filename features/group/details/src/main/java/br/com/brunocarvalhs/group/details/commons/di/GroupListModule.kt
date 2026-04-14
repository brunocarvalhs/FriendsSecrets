package br.com.brunocarvalhs.group.details.commons.di

import br.com.brunocarvalhs.group.details.app.data.repository.GroupDetailsRepositoryImpl
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
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
}
