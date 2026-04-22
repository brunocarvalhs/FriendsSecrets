package br.com.brunocarvalhs.group.draw.commons.di

import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.draw.DrawInitializerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class DrawModule {

    @Binds
    @IntoSet
    abstract fun bindDrawInitializer(impl: DrawInitializerImpl): FeatureInitializer
}