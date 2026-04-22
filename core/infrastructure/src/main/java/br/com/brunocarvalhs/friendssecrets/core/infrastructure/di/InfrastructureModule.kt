package br.com.brunocarvalhs.friendssecrets.core.infrastructure.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.biometric.BiometricManager
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.data.StorageManager
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.device.DeviceManager
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.theme.ThemeManager
import br.com.brunocarvalhs.friendssecrets.domain.services.BiometricService
import br.com.brunocarvalhs.friendssecrets.domain.services.DeviceService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.services.ThemeService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InfrastructureModule {

    @Binds
    @Singleton
    abstract fun bindStorageService(impl: StorageManager): StorageService

    @Binds
    @Singleton
    abstract fun bindBiometricService(impl: BiometricManager): BiometricService

    @Binds
    @Singleton
    abstract fun bindDeviceService(impl: DeviceManager): DeviceService

    @Binds
    @Singleton
    abstract fun bindThemeService(impl: ThemeManager): ThemeService

    companion object {
        private const val DATA_STORE_NAME = "friends_secrets_prefs"

        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile(DATA_STORE_NAME) }
            )
        }
    }
}