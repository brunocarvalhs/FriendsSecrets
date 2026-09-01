package br.com.brunocarvalhs.chat.commons.di

import android.content.Context
import androidx.room.Room
import br.com.brunocarvalhs.chat.AiGiftChatInitializerImpl
import br.com.brunocarvalhs.chat.ChatInitializerImpl
import br.com.brunocarvalhs.chat.app.data.local.ChatDatabase
import br.com.brunocarvalhs.chat.app.data.local.ChatMessageDao
import br.com.brunocarvalhs.chat.app.data.repository.ChatRepositoryImpl
import br.com.brunocarvalhs.chat.app.data.services.FirebaseAiGiftAssistantService
import br.com.brunocarvalhs.chat.app.data.services.FirebaseRealtimeManager
import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.chat.app.domain.services.AiGiftAssistantService
import br.com.brunocarvalhs.chat.app.domain.services.ChatService
import br.com.brunocarvalhs.core.navigation.FeatureInitializer
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
    @IntoSet
    abstract fun bindAiGiftChatInitializer(
        impl: AiGiftChatInitializerImpl
    ): FeatureInitializer

    @Binds
    abstract fun bindChatService(
        impl: FirebaseRealtimeManager
    ): ChatService

    @Binds
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    abstract fun bindAiGiftAssistantService(
        impl: FirebaseAiGiftAssistantService
    ): AiGiftAssistantService

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseDatabase(): FirebaseDatabase {
            return FirebaseDatabase.getInstance()
        }

        @Provides
        @Singleton
        fun provideChatDatabase(@ApplicationContext context: Context): ChatDatabase {
            return Room.databaseBuilder(context, ChatDatabase::class.java, "chat.db").build()
        }

        @Provides
        fun provideChatMessageDao(database: ChatDatabase): ChatMessageDao {
            return database.chatMessageDao()
        }
    }
}
