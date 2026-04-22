package br.com.brunocarvalhs.friendssecrets.navigation

import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavigator(impl: AppNavigator): CommonNavigator
}
