package br.com.brunocarvalhs.friendssecrets.common.di

import br.com.brunocarvalhs.friendssecrets.common.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.common.security.CryptoManager
import br.com.brunocarvalhs.friendssecrets.common.session.SessionManager
import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.CryptoService
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonsProvidesModule {

    @Provides
    @Singleton
    fun provideCryptoService(): CryptoService = CryptoManager()

    @Provides
    @Singleton
    fun bindPerformanceService(): PerformanceService = PerformanceManager.getInstance()

    @Provides
    @Singleton
    fun bindSessionService(): SessionService<UserEntities> = SessionManager.getInstance()

    @Provides
    @Singleton
    fun bindStorageService(): StorageService = StorageManager.getInstance()
}