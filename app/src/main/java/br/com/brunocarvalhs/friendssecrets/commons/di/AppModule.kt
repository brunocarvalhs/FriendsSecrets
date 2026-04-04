package br.com.brunocarvalhs.friendssecrets.commons.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoManager
import br.com.brunocarvalhs.friendssecrets.commons.storage.dataStore
import br.com.brunocarvalhs.friendssecrets.feature.groups.create.providers.GroupCreateCryptoImpl
import br.com.brunocarvalhs.friendssecrets.feature.groups.list.providers.GroupListCryptoImpl
import br.com.brunocarvalhs.group.create.commons.providers.GroupCreateCrypto
import br.com.brunocarvalhs.group.list.commons.providers.GroupListCrypto
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.ktx.performance
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Application-level dependency injection module.
 *
 * This module is responsible for providing and binding dependencies that are used
 * across the entire application. It includes configuration for Firebase services,
 * and other components needed by the app layer.
 *
 * The module is installed in the [SingletonComponent] which means the dependencies
 * provided here will have a singleton scope and will be available throughout the
 * lifetime of the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides Firebase Firestore instance.
     * Used for real-time database operations.
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    /**
     * Provides Firebase Crashlytics instance.
     * Used for crash reporting and error tracking.
     */
    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = Firebase.crashlytics

    /**
     * Provides Firebase Analytics instance.
     * Used for event tracking and user analytics.
     */
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

    /**
     * Provides Firebase Performance instance.
     * Used for performance monitoring and tracing.
     */
    @Provides
    @Singleton
    fun provideFirebasePerformance(): FirebasePerformance = Firebase.performance

    /**
     * Provides Firebase Remote Config instance.
     * Used for remote feature flags and configuration.
     */
    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideDatastore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }


    @Provides
    @Singleton
    fun provideCryptoManager(): CryptoManager = CryptoManager()

    @Provides
    @Singleton
    fun provideGroupCreateCrypto(crypto: CryptoManager): GroupCreateCrypto =
        GroupCreateCryptoImpl(crypto)

    @Provides
    @Singleton
    fun provideGroupListCrypto(crypto: CryptoManager): GroupListCrypto =
        GroupListCryptoImpl(crypto)
}
