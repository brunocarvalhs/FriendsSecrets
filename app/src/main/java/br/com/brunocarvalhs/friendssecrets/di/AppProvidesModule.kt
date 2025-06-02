package br.com.brunocarvalhs.friendssecrets.di

import android.content.Context
import android.content.SharedPreferences
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import br.com.brunocarvalhs.friendssecrets.common.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.common.extensions.getId
import br.com.brunocarvalhs.friendssecrets.common.logger.CrashLoggerProvider
import br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics.CrashlyticsProvider
import br.com.brunocarvalhs.friendssecrets.common.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.common.remote.RemoteProvider
import br.com.brunocarvalhs.friendssecrets.common.session.SessionManager
import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.initialization.providers.AnalyticsEventImpl
import br.com.brunocarvalhs.friendssecrets.initialization.providers.CrashlyticsEventImpl
import br.com.brunocarvalhs.friendssecrets.initialization.providers.PerformanceEventImpl
import br.com.brunocarvalhs.friendssecrets.initialization.providers.RemoteEventImpl
import br.com.brunocarvalhs.friendssecrets.initialization.providers.SessionEventImpl
import br.com.brunocarvalhs.friendssecrets.initialization.providers.StorageEventImpl
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppProvidesModule {

    @Provides
    @Singleton
    fun providePerformance(@ApplicationContext context: Context): PerformanceManager.PerformanceEvent {
        val performance = FirebasePerformance.getInstance().apply {
            putAttribute("deviceId", context.getId())
        }
        val event = PerformanceEventImpl(performance)
        return event
    }

    @Provides
    @Singleton
    fun provideSession(): SessionManager.SessionEvent<UserEntities> {
        val firebaseAuth = FirebaseAuth.getInstance()
        val event = SessionEventImpl(firebaseAuth)
        return event
    }

    @Provides
    @Singleton
    fun provideAnalytics(@ApplicationContext context: Context): AnalyticsProvider.AnalyticsEvent {
        val analytics = FirebaseAnalytics.getInstance(context).apply {
            setUserId(context.getId())
        }
        val event = AnalyticsEventImpl(analytics)
        return event
    }

    @Provides
    @Singleton
    fun provideCrashlytics(@ApplicationContext context: Context): CrashlyticsProvider.CrashlyticsEvent {
        val crashlytics = FirebaseCrashlytics.getInstance().apply {
            isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
            setUserId(context.getId())
        }
        val event = CrashlyticsEventImpl(crashlytics)
        return event
    }

    @Provides
    @Singleton
    fun provideRemote(): RemoteProvider.RemoteEvent {
        val remoteConfig = FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(3600)
                    .build()
            )
        }
        val event = RemoteEventImpl(remoteConfig)
        return event
    }

    @Provides
    @Singleton
    fun provideStorage(@ApplicationContext context: Context): StorageManager.StorageEvent {
        val storageName: String = BuildConfig.APPLICATION_ID
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(storageName, Context.MODE_PRIVATE)
        val event = StorageEventImpl(sharedPreferences)
        return event
    }

    @Provides
    @Singleton
    fun provideTimber(
        crashLoggerProvider: CrashLoggerProvider
    ): Timber.Forest {
        val type: Timber.Tree = if (BuildConfig.DEBUG) Timber.DebugTree()
        else crashLoggerProvider
        return Timber.apply { plant(type) }
    }
}
