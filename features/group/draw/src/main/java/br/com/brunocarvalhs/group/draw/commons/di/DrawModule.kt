package br.com.brunocarvalhs.group.draw.commons.di

import br.com.brunocarvalhs.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.draw.DrawInitializerImpl
import br.com.brunocarvalhs.group.draw.app.data.repository.DrawRepositoryImpl
import br.com.brunocarvalhs.group.draw.app.domain.repository.DrawRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DrawModule {

    @Binds
    @IntoSet
    abstract fun bindDrawInitializer(
        impl: DrawInitializerImpl
    ): FeatureInitializer

    @Binds
    abstract fun bindDrawRepository(
        impl: DrawRepositoryImpl
    ): DrawRepository
}
