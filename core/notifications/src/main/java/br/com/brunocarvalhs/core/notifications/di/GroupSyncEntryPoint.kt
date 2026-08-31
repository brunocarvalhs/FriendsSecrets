package br.com.brunocarvalhs.core.notifications.di

import br.com.brunocarvalhs.core.network.domain.NetworkService
import br.com.brunocarvalhs.storage.domain.StorageService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface GroupSyncEntryPoint {
    fun networkService(): NetworkService
    fun storageService(): StorageService
}
