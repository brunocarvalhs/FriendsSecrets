package br.com.brunocarvalhs.friendssecrets.di

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.commons.initialization.AppInitializationManager
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.AnalyticsInitialization
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.CrashlyticsInitialization
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.RemoteInitialization
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.TimberInitialization
import br.com.brunocarvalhs.friendssecrets.commons.logger.CrashLoggerProvider
import br.com.brunocarvalhs.friendssecrets.commons.logger.crashlytics.CrashlyticsProvider
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.commons.remote.RemoteProvider
import br.com.brunocarvalhs.friendssecrets.commons.remote.theme.ThemeRemoteProvider
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.commons.security.BiometricManager
import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.commons.theme.ThemeManager
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
        return AnalyticsProvider
    }

    @Provides
    @Singleton
    fun provideCrashLoggerProvider(): CrashLoggerProvider {
        return CrashlyticsProvider()
    }

    @Provides
    @Singleton
    fun provideRemoteProvider(): RemoteProvider {
        return RemoteProvider()
    }

    @Provides
    @Singleton
    fun provideThemeRemoteProvider(@ApplicationContext context: Context): ThemeRemoteProvider {
        return ThemeRemoteProvider(context)
    }

    @Provides
    @Singleton
    fun provideToggleManager(@ApplicationContext context: Context): ToggleManager {
        return ToggleManager(context)
    }

    @Provides
    @Singleton
    fun providePerformanceManager(): PerformanceManager {
        return PerformanceManager()
    }

    @Provides
    @Singleton
    fun provideBiometricManager(@ApplicationContext context: Context): BiometricManager {
        return BiometricManager(context)
    }

    @Provides
    @Singleton
    fun provideThemeManager(@ApplicationContext context: Context): ThemeManager {
        return ThemeManager(context)
    }

    @Provides
    @Singleton
    fun provideCryptoService(): CryptoService {
        return CryptoService()
    }
}