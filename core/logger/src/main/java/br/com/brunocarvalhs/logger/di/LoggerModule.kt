package br.com.brunocarvalhs.logger.di

import br.com.brunocarvalhs.logger.CrashlyticsLogger
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics =
        Firebase.crashlytics

    @Provides
    @Singleton
    fun provideCrashlyticsLogger(
        crashlytics: FirebaseCrashlytics
    ): CrashlyticsLogger {
        return CrashlyticsLogger(crashlytics)
    }
}