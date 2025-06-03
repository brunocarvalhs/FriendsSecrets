package br.com.brunocarvalhs.friendssecrets.common.di

import br.com.brunocarvalhs.friendssecrets.common.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics.CrashlyticsProvider
import br.com.brunocarvalhs.friendssecrets.common.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.common.remote.RemoteProvider
import br.com.brunocarvalhs.friendssecrets.common.security.BiometricManager
import br.com.brunocarvalhs.friendssecrets.common.security.CryptoManager
import br.com.brunocarvalhs.friendssecrets.common.session.SessionManager
import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager // Supondo que StorageManager também não precise de getInstance()
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
object CommonsProvidesModule {

    @Provides
    @Singleton
    fun provideCryptoService(): CryptoService = CryptoManager()

    @Provides
    @Singleton
    fun providePerformanceService(event: PerformanceManager.PerformanceEvent): PerformanceService {
        return PerformanceManager(event)
    }

    @Provides
    @Singleton
    fun provideSessionService(event: SessionManager.SessionEvent<UserEntities>): SessionService<UserEntities> {
        return SessionManager(event)
    }

    @Provides
    @Singleton
    fun provideStorageService(event: StorageManager.StorageEvent): StorageService {
        return StorageManager(event)
    }

    @Provides
    @Singleton
    fun provideBiometricManager(storage: StorageManager): BiometricManager {
        return BiometricManager(storage)
    }

    @Provides
    @Singleton
    fun provideRemoteProvider(event: RemoteProvider.RemoteEvent): RemoteProvider {
        return RemoteProvider(event)
    }

    @Provides
    @Singleton
    fun provideCrashlytics(event: CrashlyticsProvider.CrashlyticsEvent): CrashlyticsProvider {
        return CrashlyticsProvider(event)
    }

    @Provides
    @Singleton
    fun provideAnalyticsProvider(event: AnalyticsProvider.AnalyticsEvent): AnalyticsProvider {
        return AnalyticsProvider(event)
    }
}
