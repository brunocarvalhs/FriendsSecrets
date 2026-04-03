package br.com.brunocarvalhs.friendssecrets.data.model

import com.google.firebase.perf.metrics.AddTrace
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

/**
 * Data model for anonymous users
 * No personal data like name, phone, or photos are stored
 */
@Deprecated("Use features.group.app.data.model.UserModel instead")
@Serializable
internal data class UserModel(
    @SerialName(UserEntities.ID) override val id: String = UUID.randomUUID().toString(),
    @SerialName(UserEntities.LIKES) override val likes: List<String> = emptyList(),
) : UserEntities {

    @SerialName(UserEntities.NAME)
    override val name: String = ""

    @SerialName(UserEntities.PHONE_NUMBER)
    override val phoneNumber: String = ""

    @SerialName(UserEntities.PHOTO_URL)
    override val photoUrl: String? = null

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
            )
        }
    }
}

@Deprecated("Use UserEntities.Companion.create instead")
@AddTrace(name = "UserEntities.create", enabled = true)
fun UserEntities.Companion.create(
    id: String = UUID.randomUUID().toString(),
    likes: List<String> = emptyList(),
): UserEntities = UserModel(
    id = id,
    likes = likes,
)
