package br.com.brunocarvalhs.friendssecrets.core.infrastructure.di

import br.com.brunocarvalhs.friendssecrets.core.infrastructure.config.FeatureFlagsManager
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.config.RemoteConfigService
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.ConfigurationService
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.FeatureFlagService
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.ThemeRemote
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.ThemeService
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.theme.ThemeManager
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.theme.remote.ThemeRemoteProvider
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RemoteModule {

    @Binds
    @Singleton
    abstract fun bindThemeService(
        impl: ThemeManager
    ): ThemeService

    @Binds
    @Singleton
    abstract fun bindThemeRemote(
        impl: ThemeRemoteProvider
    ): ThemeRemote

    @Binds
    @Singleton
    abstract fun bindFeatureFlags(
        impl: FeatureFlagsManager
    ): FeatureFlagService

    @Binds
    @Singleton
    abstract fun bindRemoteConfigService(
        impl: RemoteConfigService
    ): ConfigurationService

    companion object {
        @Provides
        @Singleton
        fun provideRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig
    }
}
