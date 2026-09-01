package br.com.brunocarvalhs.core.featureflags.di

import br.com.brunocarvalhs.core.featureflags.data.AndroidAppVersionProvider
import br.com.brunocarvalhs.core.featureflags.data.VersionedFeatureFlagsManager
import br.com.brunocarvalhs.core.featureflags.domain.AppVersionProvider
import br.com.brunocarvalhs.core.featureflags.domain.VersionedFeatureFlagService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class FeatureFlagsModule {

    @Binds
    @Singleton
    abstract fun bindAppVersionProvider(
        impl: AndroidAppVersionProvider
    ): AppVersionProvider

    @Binds
    @Singleton
    abstract fun bindVersionedFeatureFlagService(
        impl: VersionedFeatureFlagsManager
    ): VersionedFeatureFlagService
}
