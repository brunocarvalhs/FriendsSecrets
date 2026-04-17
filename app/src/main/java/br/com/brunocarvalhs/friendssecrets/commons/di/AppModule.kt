package br.com.brunocarvalhs.friendssecrets.commons.di

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.commons.network.FirebaseCompatibilityConverter
import br.com.brunocarvalhs.friendssecrets.commons.network.FirebaseFirestoreManager
import br.com.brunocarvalhs.friendssecrets.commons.network.FirebaseRealtimeManager
import br.com.brunocarvalhs.friendssecrets.commons.network.NetworkManager
import br.com.brunocarvalhs.friendssecrets.commons.network.RemoteConfigService
import br.com.brunocarvalhs.friendssecrets.commons.providers.GroupImageManager
import br.com.brunocarvalhs.friendssecrets.commons.security.BiometricManager
import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoManager
import br.com.brunocarvalhs.friendssecrets.commons.security.DeviceManager
import br.com.brunocarvalhs.friendssecrets.commons.storage.StorageManager
import br.com.brunocarvalhs.friendssecrets.commons.storage.dataStore
import br.com.brunocarvalhs.friendssecrets.commons.theme.ThemeManager
import br.com.brunocarvalhs.friendssecrets.commons.theme.remote.ThemeRemoteProvider
import br.com.brunocarvalhs.friendssecrets.domain.services.BiometricService
import br.com.brunocarvalhs.friendssecrets.domain.services.ChatService
import br.com.brunocarvalhs.friendssecrets.domain.services.ConfigurationService
import br.com.brunocarvalhs.friendssecrets.domain.services.CryptoService
import br.com.brunocarvalhs.friendssecrets.domain.services.DeviceService
import br.com.brunocarvalhs.friendssecrets.domain.services.GroupImageService
import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.services.ThemeService
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
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

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = Firebase.database("https://friends-secrets-1642275671992-default-rtdb.asia-southeast1.firebasedatabase.app")

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
    fun provideStorageManager(
        @ApplicationContext context: Context
    ): StorageService {
        return StorageManager(context.dataStore)
    }

    @Provides
    @Singleton
    fun provideCryptoManager(): CryptoManager = CryptoManager()

    @Provides
    @Singleton
    fun provideNetworkManager(
        firebaseFirestoreManager: FirebaseFirestoreManager,
        cryptoManager: CryptoManager,
        compatibilityConverter: FirebaseCompatibilityConverter
    ): NetworkService = NetworkManager(
        firebaseFirestoreManager = firebaseFirestoreManager,
        cryptoManager = cryptoManager,
        compatibilityConverter = compatibilityConverter
    )

    @Provides
    @Singleton
    fun provideChatService(
        firebaseRealtimeManager: FirebaseRealtimeManager
    ): ChatService = firebaseRealtimeManager

    @Provides
    @Singleton
    fun provideDeviceService(service: DeviceManager): DeviceService = service

    @Provides
    @Singleton
    fun provideThemeService(service: ThemeManager): ThemeService = service

    @Provides
    @Singleton
    fun provideThemeRemoteProvider(
        firebaseRemoteConfig: FirebaseRemoteConfig
    ): ThemeRemoteProvider = ThemeRemoteProvider(
        remoteProvider = firebaseRemoteConfig
    )

    @Provides
    @Singleton
    fun provideBiometricService(
        @ApplicationContext context: Context,
        storageService: StorageService
    ): BiometricService = BiometricManager(context, storageService)

    @Provides
    @Singleton
    fun provideCryptoService(): CryptoService {
        return CryptoManager()
    }

    @Provides
    @Singleton
    fun provideGroupImageService(service: GroupImageManager): GroupImageService = service

    @Provides
    @Singleton
    fun provideConfigurationService(
        remoteConfig: FirebaseRemoteConfig
    ): ConfigurationService = RemoteConfigService(remoteConfig)
}
