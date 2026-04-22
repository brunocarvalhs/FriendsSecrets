package br.com.brunocarvalhs.settings.commons.di

import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.settings.SettingsInitializerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    @IntoSet
    abstract fun bindSettingsInitializer(impl: SettingsInitializerImpl): FeatureInitializer
}