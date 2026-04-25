package br.com.brunocarvalhs.chat.commons.di

import br.com.brunocarvalhs.chat.ChatInitializerImpl
import br.com.brunocarvalhs.chat.app.data.repository.ChatRepositoryImpl
import br.com.brunocarvalhs.chat.app.data.services.FirebaseRealtimeManager
import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.chat.app.domain.services.ChatService
import br.com.brunocarvalhs.chat.commons.analytics.ChatAnalytics
import br.com.brunocarvalhs.chat.commons.analytics.ChatAnalyticsImpl
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    @IntoSet
    abstract fun bindChatInitializer(
        impl: ChatInitializerImpl
    ): FeatureInitializer

    @Binds
    abstract fun bindChatService(
        impl: FirebaseRealtimeManager
    ): ChatService

    @Binds
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository

    companion object {

        @Provides
        fun provideChatAnalytics(
            firebaseAnalytics: FirebaseAnalytics
        ): ChatAnalytics {
            return ChatAnalyticsImpl(firebaseAnalytics)
        }

        @Provides
        @Singleton
        fun provideFirebaseDatabase(): FirebaseDatabase {
            return FirebaseDatabase.getInstance().apply {
                setPersistenceEnabled(true)
            }
        }
    }
}
