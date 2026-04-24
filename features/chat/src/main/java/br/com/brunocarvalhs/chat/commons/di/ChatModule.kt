package br.com.brunocarvalhs.chat.commons.di

import android.content.Context
import androidx.room.Room
import br.com.brunocarvalhs.chat.ChatInitializerImpl
import br.com.brunocarvalhs.chat.app.data.local.ChatDatabase
import br.com.brunocarvalhs.chat.app.data.local.ChatMessageDao
import br.com.brunocarvalhs.chat.app.data.repository.ChatRepositoryImpl
import br.com.brunocarvalhs.chat.app.data.services.FirebaseRealtimeManager
import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.chat.app.domain.services.ChatService
import br.com.brunocarvalhs.chat.commons.analytics.ChatAnalytics
import br.com.brunocarvalhs.chat.commons.analytics.ChatAnalyticsImpl
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    companion object {

        @Provides
        fun provideChatDatabase(
            @ApplicationContext context: Context
        ): ChatDatabase {
            return Room.databaseBuilder(
                context,
                ChatDatabase::class.java,
                "chat_db"
            ).build()
        }

        @Provides
        fun provideChatMessageDao(
            database: ChatDatabase
        ): ChatMessageDao {
            return database.chatMessageDao()
        }

        @Provides
        fun provideChatAnalytics(
            firebaseAnalytics: FirebaseAnalytics
        ): ChatAnalytics {
            return ChatAnalyticsImpl(firebaseAnalytics)
        }

        @Provides
        fun bindChatService(
            chatService: ChatService,
            networkService: NetworkService,
            chatMessageDao: ChatMessageDao
        ): ChatRepository {
            return ChatRepositoryImpl(chatService, networkService, chatMessageDao)
        }

        @Provides
        @Singleton
        fun provideFirebaseDatabase(): FirebaseDatabase {
            return FirebaseDatabase.getInstance()
        }
    }
}