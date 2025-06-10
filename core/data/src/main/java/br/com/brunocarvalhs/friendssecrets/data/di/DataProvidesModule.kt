package br.com.brunocarvalhs.friendssecrets.data.di

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.common.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.common.logger.CrashLoggerProvider
import br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics.CrashlyticsProvider
import br.com.brunocarvalhs.friendssecrets.common.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.common.remote.RemoteProvider
import br.com.brunocarvalhs.friendssecrets.common.session.SessionManager
import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager
import br.com.brunocarvalhs.friendssecrets.data.BuildConfig
import br.com.brunocarvalhs.friendssecrets.data.initialization.providers.AnalyticsEventImpl
import br.com.brunocarvalhs.friendssecrets.data.initialization.providers.CrashlyticsEventImpl
import br.com.brunocarvalhs.friendssecrets.data.initialization.providers.PerformanceEventImpl
import br.com.brunocarvalhs.friendssecrets.data.initialization.providers.RemoteEventImpl
import br.com.brunocarvalhs.friendssecrets.data.initialization.providers.SessionEventImpl
import br.com.brunocarvalhs.friendssecrets.data.initialization.providers.StorageEventImpl
import br.com.brunocarvalhs.friendssecrets.data.service.GenerativeServiceImpl
import br.com.brunocarvalhs.friendssecrets.data.service.PhoneAuthServiceImpl
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.GenerativeService
import br.com.brunocarvalhs.friendssecrets.domain.services.PhoneAuthService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.ktx.performance
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataProvidesModule {

    @Provides
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = Firebase.crashlytics

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideFirebaseAuthentication(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideFirebasePerformance(): FirebasePerformance = Firebase.performance

    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig

    @Provides
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

    @Provides
    fun provideGenerativeModel(): GenerativeModel = GenerativeModel(
        BuildConfig.AI_API_KEY, BuildConfig.AI_MODEL_NAME
    )

    @Provides
    fun providePhoneAuthService(
        firebaseAuthProvider: Lazy<FirebaseAuth>
    ): PhoneAuthService {
        return PhoneAuthServiceImpl(firebaseAuthProvider)
    }

    @Provides
    fun provideGenerativeService(
        generativeModel: Lazy<GenerativeModel>
    ): GenerativeService {
        return GenerativeServiceImpl(generativeModel)
    }

    @Provides
    @Singleton
    fun providePerformance(
        performanceProvider: Lazy<FirebasePerformance>,
    ): PerformanceManager.PerformanceEvent {
        return PerformanceEventImpl(performanceProvider)
    }

    @Provides
    @Singleton
    fun provideSession(
        firebaseAuthProvider: FirebaseAuth,
        storageManager: StorageService
    ): SessionManager.SessionEvent<UserEntities> {
        return SessionEventImpl(firebaseAuthProvider, storageManager)
    }

    @Provides
    @Singleton
    fun provideAnalytics(analyticsProvider: Lazy<FirebaseAnalytics>): AnalyticsProvider.AnalyticsEvent {
        return AnalyticsEventImpl(analyticsProvider)
    }

    @Provides
    @Singleton
    fun provideCrashlytics(crashlyticsProvider: Lazy<FirebaseCrashlytics>): CrashlyticsProvider.CrashlyticsEvent {
        return CrashlyticsEventImpl(crashlyticsProvider)
    }

    @Provides
    @Singleton
    fun provideRemote(remoteConfigProvider: Lazy<FirebaseRemoteConfig>): RemoteProvider.RemoteEvent {
        return RemoteEventImpl(remoteConfigProvider)
    }

    @Provides
    @Singleton
    fun provideStorage(@ApplicationContext context: Context): StorageManager.StorageEvent {
        return StorageEventImpl(context)
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
