package br.com.brunocarvalhs.chat.commons.di

import br.com.brunocarvalhs.chat.ChatInitializerImpl
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    @IntoSet
    abstract fun bindChatInitializer(impl: ChatInitializerImpl): FeatureInitializer
}