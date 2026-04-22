package br.com.brunocarvalhs.friendssecrets.core.infrastructure.di

import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.ThemeService
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.theme.ThemeManager
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.theme.remote.ThemeRemoteProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ThemeRemoteModule {

    @Binds
    @Singleton
    abstract fun bindThemeService(
        impl: ThemeManager
    ): ThemeService

    companion object {

        @Provides
        @Singleton
        @JvmStatic
        fun provideThemeRemoteProvider(
            firebaseRemoteConfig: FirebaseRemoteConfig
        ): ThemeRemoteProvider {
            return ThemeRemoteProvider(
                remoteProvider = firebaseRemoteConfig
            )
        }
    }
}
