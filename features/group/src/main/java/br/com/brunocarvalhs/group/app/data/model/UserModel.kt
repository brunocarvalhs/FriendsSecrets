package br.com.brunocarvalhs.group.app.data.model

import br.com.brunocarvalhs.group.app.domain.entities.UserEntities
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.UUID

/**
 * Data model for anonymous users
 * No personal data like name, phone, or photos are stored
 */
@Serializable
internal data class UserModel(
    @SerialName(UserEntities.ID) override val id: String = UUID.randomUUID().toString(),
    @SerialName(UserEntities.LIKES) override val likes: List<String> = emptyList(),
    @SerialName(UserEntities.NAME) override val name: String = "",
    @SerialName(UserEntities.PHONE_NUMBER) override val phoneNumber: String = "",
    @SerialName(UserEntities.PHOTO_URL) override val photoUrl: String? = null,
) : UserEntities {

    @AddTrace(name = "UserModel.toMap", enabled = true)
    override fun toMap(): Map<String, Any> {
        return mapOf(
            UserEntities.ID to id,
            UserEntities.LIKES to likes,
        )
    }

    @AddTrace(name = "UserModel.toJson", enabled = true)
    override fun toJson(): String {
        return Json.encodeToString(this)
    }

    @AddTrace(name = "UserModel.toCopy", enabled = true)
    override fun toCopy(
        id: String,
        likes: List<String>,
    ): UserEntities = copy(
        id = id,
        likes = likes,
    )

    companion object {
        @AddTrace(name = "UserModel.fromMap", enabled = true)
        fun fromMap(map: Map<String, Any?>): UserEntities {
            return UserModel(
                id = map[UserEntities.ID] as? String ?: UUID.randomUUID().toString(),
                likes = (map[UserEntities.LIKES] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                name = map[UserEntities.NAME] as? String ?: "",
                phoneNumber = map[UserEntities.PHONE_NUMBER] as? String ?: "",
                photoUrl = map[UserEntities.PHOTO_URL] as? String
            )
        }
    }
}
