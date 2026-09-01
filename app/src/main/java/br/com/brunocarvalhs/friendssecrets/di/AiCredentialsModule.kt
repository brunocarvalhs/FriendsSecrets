package br.com.brunocarvalhs.friendssecrets.di

import br.com.brunocarvalhs.core.domain.services.AiCredentialsProvider
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiCredentialsModule {

    @Provides
    @Singleton
    fun provideAiCredentialsProvider(): AiCredentialsProvider = object : AiCredentialsProvider {
        override fun getOpenRouterApiKey(): String = BuildConfig.OPEN_ROUTER_API_KEY
    }
}
