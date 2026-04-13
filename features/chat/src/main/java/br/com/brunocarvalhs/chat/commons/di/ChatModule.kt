package br.com.brunocarvalhs.chat.commons.di

import android.content.Context
import androidx.room.Room
import br.com.brunocarvalhs.chat.app.data.local.ChatDatabase
import br.com.brunocarvalhs.chat.app.data.local.ChatMessageDao
import br.com.brunocarvalhs.chat.app.data.repository.ChatRepositoryImpl
import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideChatDatabase(@ApplicationContext context: Context): ChatDatabase {
        return Room.databaseBuilder(
            context,
            ChatDatabase::class.java,
            "chat_database"
        ).build()
    }

    @Provides
    fun provideChatMessageDao(database: ChatDatabase): ChatMessageDao = database.chatMessageDao()

    @Provides
    @Singleton
    fun provideChatRepository(
        database: FirebaseDatabase,
        chatMessageDao: ChatMessageDao
    ): ChatRepository = ChatRepositoryImpl(database, chatMessageDao)
}
