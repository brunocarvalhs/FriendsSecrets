package br.com.brunocarvalhs.friendssecrets.core.navigation.di

import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.friendssecrets.core.navigation.navigation.AppNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.Multibinds
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Multibinds
    abstract fun featureInitializers(): Set<FeatureInitializer>

    @Binds
    @Singleton
    abstract fun bindNavigator(impl: AppNavigator): CommonNavigator
}
