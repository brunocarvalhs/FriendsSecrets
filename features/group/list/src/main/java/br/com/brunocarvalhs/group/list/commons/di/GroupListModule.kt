package br.com.brunocarvalhs.group.list.commons.di

import br.com.brunocarvalhs.group.list.app.data.repository.GroupListRepositoryImpl
import br.com.brunocarvalhs.group.list.app.data.services.StorageServiceImpl
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.group.list.app.domain.services.StorageService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupListModule {

    @Binds
    abstract fun bindGroupListRepository(
        impl: GroupListRepositoryImpl
    ): GroupListRepository

    @Binds
    @Singleton
    abstract fun bindStorageService(
        impl: StorageServiceImpl
    ): StorageService
}
