package br.com.brunocarvalhs.friendssecrets.core.infrastructure.di

import br.com.brunocarvalhs.friendssecrets.core.infrastructure.initializer.FirebaseInitializerImpl
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.logger.CrashlyticsLogger
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.storage.DataStoreService
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.theme.ThemeManager
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.theme.remote.ThemeRemoteProvider
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.services.ThemeService
import br.com.brunocarvalhs.friendssecrets.domain.services.logger.LoggerService
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.ktx.performance
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InfrastructureModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = Firebase.database("https://friends-secrets-1642275671992-default-rtdb.asia-southeast1.firebasedatabase.app")

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = Firebase.crashlytics

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

    @Provides
    @Singleton
    fun provideFirebasePerformance(): FirebasePerformance = Firebase.performance

    @Provides
    @Singleton
    fun provideThemeRemoteProvider(
        firebaseRemoteConfig: FirebaseRemoteConfig
    ): ThemeRemoteProvider = ThemeRemoteProvider(
        remoteProvider = firebaseRemoteConfig
    )

    @Binds
    @Singleton
    abstract fun bindThemeService(impl: ThemeManager): ThemeService

    @Binds
    @Singleton
    abstract fun bindLoggerService(impl: CrashlyticsLogger): LoggerService

    @Binds
    @Singleton
    abstract fun bindStorageService(impl: DataStoreService): StorageService

    @Binds
    @IntoSet
    abstract fun bindFirebaseInitializer(impl: FirebaseInitializerImpl): FeatureInitializer
}
