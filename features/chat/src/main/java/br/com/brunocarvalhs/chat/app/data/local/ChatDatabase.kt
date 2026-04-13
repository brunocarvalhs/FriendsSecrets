package br.com.brunocarvalhs.chat.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.brunocarvalhs.chat.app.data.model.ChatMessage

@Database(entities = [ChatMessage::class], version = 1, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
}
