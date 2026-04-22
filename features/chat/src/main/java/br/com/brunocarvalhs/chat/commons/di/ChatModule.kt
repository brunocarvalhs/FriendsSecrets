package br.com.brunocarvalhs.chat.commons.di

import android.content.Context
import androidx.room.Room
import br.com.brunocarvalhs.chat.app.data.local.ChatDatabase
import br.com.brunocarvalhs.chat.app.data.local.ChatMessageDao
import br.com.brunocarvalhs.chat.app.data.repository.ChatRepositoryImpl
import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.chat.commons.analytics.ChatAnalytics
import br.com.brunocarvalhs.chat.commons.analytics.ChatAnalyticsImpl
import br.com.brunocarvalhs.chat.app.data.services.FirebaseRealtimeManager
import br.com.brunocarvalhs.chat.app.domain.services.ChatService
import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    @Singleton
    internal abstract fun bindChatAnalytics(impl: ChatAnalyticsImpl): ChatAnalytics

    companion object {
        @Provides
        @Singleton
        fun provideChatDatabase(@ApplicationContext context: Context): ChatDatabase {
            return Room.databaseBuilder(
                context,
                ChatDatabase::class.java,
                "chat_database"
            )
            .fallbackToDestructiveMigration()
            .build()
        }

        @Provides
        fun provideChatMessageDao(database: ChatDatabase): ChatMessageDao = database.chatMessageDao()

        @Provides
        @Singleton
        fun provideChatRepository(
            chatService: ChatService,
            networkService: NetworkService,
            chatMessageDao: ChatMessageDao
        ): ChatRepository = ChatRepositoryImpl(chatService, networkService, chatMessageDao)
    }
}
