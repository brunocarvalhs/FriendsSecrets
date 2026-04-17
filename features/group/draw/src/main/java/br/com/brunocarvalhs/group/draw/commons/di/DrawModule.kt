package br.com.brunocarvalhs.group.draw.commons.di

import br.com.brunocarvalhs.friendssecrets.domain.services.DrawService
import br.com.brunocarvalhs.group.draw.app.data.repository.DrawRepositoryImpl
import br.com.brunocarvalhs.group.draw.app.data.services.DrawManager
import br.com.brunocarvalhs.group.draw.app.domain.repository.DrawRepository
import br.com.brunocarvalhs.group.draw.commons.analytics.DrawAnalytics
import br.com.brunocarvalhs.group.draw.commons.analytics.DrawAnalyticsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DrawModule {

    @Binds
    abstract fun bindDrawRepository(
        impl: DrawRepositoryImpl
    ): DrawRepository

    @Binds
    abstract fun bindDrawService(
        impl: DrawManager
    ): DrawService

    @Binds
    abstract fun bindDrawAnalytics(
        impl: DrawAnalyticsImpl
    ): DrawAnalytics
}