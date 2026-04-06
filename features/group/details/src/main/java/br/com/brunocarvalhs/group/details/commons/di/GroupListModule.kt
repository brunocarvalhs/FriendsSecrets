package br.com.brunocarvalhs.group.details.commons.di

import br.com.brunocarvalhs.group.list.app.data.services.StorageServiceImpl
import br.com.brunocarvalhs.group.details.app.domain.services.StorageService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupDetailsModule {

    @Binds
    abstract fun bindGroupListRepository(
        impl: GroupDetailsRepositoryImpl
    ): GroupDetailsRepository

    @Binds
    @Singleton
    abstract fun bindStorageService(
        impl: StorageServiceImpl
    ): StorageService
}
