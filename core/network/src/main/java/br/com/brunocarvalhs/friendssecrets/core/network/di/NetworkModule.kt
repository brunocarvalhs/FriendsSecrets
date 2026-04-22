package br.com.brunocarvalhs.friendssecrets.core.network.di

import br.com.brunocarvalhs.friendssecrets.core.network.config.FeatureFlagsManager
import br.com.brunocarvalhs.friendssecrets.core.network.config.RemoteConfigService
import br.com.brunocarvalhs.friendssecrets.core.network.data.NetworkManager
import br.com.brunocarvalhs.friendssecrets.domain.services.ConfigurationService
import br.com.brunocarvalhs.friendssecrets.domain.services.FeatureFlagService
import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindNetworkService(impl: NetworkManager): NetworkService

    @Binds
    @Singleton
    abstract fun bindConfigurationService(impl: RemoteConfigService): ConfigurationService

    @Binds
    @Singleton
    abstract fun bindFeatureFlagService(impl: FeatureFlagsManager): FeatureFlagService

    companion object {
        @Provides
        @Singleton
        fun provideFirestore(): FirebaseFirestore = Firebase.firestore

        @Provides
        @Singleton
        fun provideRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig
    }
}