package br.com.brunocarvalhs.storage.di

import br.com.brunocarvalhs.storage.data.StorageManager
import br.com.brunocarvalhs.storage.domain.StorageService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    @Singleton
    internal abstract fun bindStorageService(impl: StorageManager): StorageService
}
