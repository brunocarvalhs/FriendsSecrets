package br.com.brunocarvalhs.friendssecrets.core.navigation.di

import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.Multibinds

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Multibinds
    abstract fun featureInitializers(): Set<FeatureInitializer>
}