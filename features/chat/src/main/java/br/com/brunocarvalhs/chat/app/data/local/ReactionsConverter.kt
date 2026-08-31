package br.com.brunocarvalhs.chat.app.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class ReactionsConverter {

    @TypeConverter
    fun fromReactions(reactions: Map<String, String>): String {
        return Json.encodeToString(reactions)
    }

    @TypeConverter
    fun toReactions(value: String): Map<String, String> {
        return runCatching { Json.decodeFromString<Map<String, String>>(value) }
            .getOrDefault(emptyMap())
    }
}
