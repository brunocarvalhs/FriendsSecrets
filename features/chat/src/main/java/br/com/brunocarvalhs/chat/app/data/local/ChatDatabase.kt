package br.com.brunocarvalhs.chat.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.brunocarvalhs.chat.app.data.model.ChatMessage

@Database(entities = [ChatMessage::class], version = 3, exportSchema = false)
@TypeConverters(ReactionsConverter::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
}
