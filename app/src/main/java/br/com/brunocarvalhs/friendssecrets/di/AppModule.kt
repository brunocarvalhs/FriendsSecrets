package br.com.brunocarvalhs.friendssecrets.di

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.commons.initialization.AppInitializationManager
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.AnalyticsInitialization
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.CrashlyticsInitialization
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.RemoteInitialization
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.TimberInitialization
import br.com.brunocarvalhs.friendssecrets.commons.logger.CrashLoggerProvider
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.commons.remote.RemoteProvider
import br.com.brunocarvalhs.friendssecrets.commons.remote.theme.ThemeRemoteProvider
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.commons.security.BiometricManager
import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.commons.theme.ThemeManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppInitializationManager(@ApplicationContext context: Context): AppInitializationManager {
        return AppInitializationManager(
            initializations = listOf(
                RemoteInitialization(),
                TimberInitialization(),
                CrashlyticsInitialization(context),
                AnalyticsInitialization(context)
            )
        )
    }

    @Provides
    @Singleton
    fun provideAnalyticsProvider(): AnalyticsProvider {
        return AnalyticsProvider()
    }

    @Provides
    @Singleton
    fun provideCrashLoggerProvider(): CrashLoggerProvider {
        return CrashLoggerProvider()
    }

    @Provides
    @Singleton
    fun provideRemoteProvider(): RemoteProvider {
        return RemoteProvider()
    }

    @Provides
    @Singleton
    fun provideThemeRemoteProvider(
        remoteProvider: RemoteProvider,
    ): ThemeRemoteProvider {
        return ThemeRemoteProvider(
            remoteProvider = remoteProvider,
        )
    }

    @Provides
    @Singleton
    fun provideToggleManager(
        remoteProvider: RemoteProvider
    ): ToggleManager {
        return ToggleManager(remoteProvider = remoteProvider)
    }

    @Provides
    @Singleton
    fun providePerformanceManager(): PerformanceManager {
        return PerformanceManager()
    }

    @Provides
    @Singleton
    fun provideBiometricManager(): BiometricManager {
        return BiometricManager
    }

    @Provides
    @Singleton
    fun provideThemeManager(): ThemeManager {
        return ThemeManager
    }

    @Provides
    @Singleton
    fun provideCryptoService(): CryptoService {
        return CryptoService()
    }
}