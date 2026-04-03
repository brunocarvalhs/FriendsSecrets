package br.com.brunocarvalhs.group.app.data.repositories.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import java.util.UUID

@Serializable
internal data class UserDTO(
    @SerialName(UserEntities.ID) val id: String = UUID.randomUUID().toString(),
    @SerialName(UserEntities.NAME) val name: String,
    @SerialName(UserEntities.PHOTO_URL) val photoUrl: String? = null,
    @SerialName(UserEntities.PHONE_NUMBER) val phoneNumber: String = "",
    @SerialName(UserEntities.LIKES) val likes: List<String> = emptyList(),
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            UserEntities.ID to id,
            UserEntities.NAME to name,
            UserEntities.PHOTO_URL to photoUrl.orEmpty(),
            UserEntities.PHONE_NUMBER to phoneNumber,
            UserEntities.LIKES to likes
        )
    }

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun fromMap(map: Map<String, Any>): UserDTO {
            return json.decodeFromJsonElement(json.encodeToJsonElement(map))
        }
    }
}
