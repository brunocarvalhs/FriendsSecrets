package br.com.brunocarvalhs.group.draw.commons.di

import br.com.brunocarvalhs.group.draw.app.data.repository.DrawRepositoryImpl
import br.com.brunocarvalhs.group.draw.app.domain.repository.DrawRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DrawModule {

    @Binds
    abstract fun bindDrawRepository(
        impl: DrawRepositoryImpl
    ): DrawRepository
}