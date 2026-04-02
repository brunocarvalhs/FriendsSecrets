package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.firebase.perf.metrics.AddTrace
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.UUID

/**
 * Data model for anonymous users
 * No personal data like name, phone, or photos are stored
 */
internal data class UserModel(
    @SerializedName(UserEntities.ID) override val id: String = UUID.randomUUID().toString(),
    @SerializedName(UserEntities.LIKES) override val likes: List<String> = emptyList(),
    @SerializedName(UserEntities.IS_ANONYMOUS) override val isAnonymous: Boolean = true,
    @SerializedName(UserEntities.LAST_LOGIN) override val lastLogin: Long = System.currentTimeMillis(),
    @SerializedName(UserEntities.IS_ACTIVE) override val isActive: Boolean = true
) : UserEntities {

    @AddTrace(name = "UserModel.toMap", enabled = true)
    override fun toMap(): Map<String, Any> {
        return mapOf(
            UserEntities.ID to id,
            UserEntities.LIKES to likes,
            UserEntities.IS_ANONYMOUS to isAnonymous,
            UserEntities.LAST_LOGIN to lastLogin,
            UserEntities.IS_ACTIVE to isActive
        )
    }

    @AddTrace(name = "UserModel.toJson", enabled = true)
    override fun toJson(): String {
        return gson.toJson(this)
    }

    @AddTrace(name = "UserModel.toCopy", enabled = true)
    override fun toCopy(
        id: String,
        likes: List<String>,
        isAnonymous: Boolean,
        lastLogin: Long,
        isActive: Boolean
    ): UserEntities = copy(
        id = id,
        likes = likes,
        isAnonymous = isAnonymous,
        lastLogin = lastLogin,
        isActive = isActive
    )

    companion object {
        private val gson = Gson()

        @AddTrace(name = "UserModel.fromMap", enabled = true)
        fun fromMap(map: Map<String, Any?>): UserEntities {
            return UserModel(
                id = map[UserEntities.ID] as? String ?: UUID.randomUUID().toString(),
                likes = (map[UserEntities.LIKES] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                isAnonymous = map[UserEntities.IS_ANONYMOUS] as? Boolean ?: true,
                lastLogin = (map[UserEntities.LAST_LOGIN] as? Number)?.toLong() ?: System.currentTimeMillis(),
                isActive = map[UserEntities.IS_ACTIVE] as? Boolean ?: true
            )
        }
    }
}

@AddTrace(name = "UserEntities.create", enabled = true)
fun UserEntities.Companion.create(
    id: String = UUID.randomUUID().toString(),
    likes: List<String> = emptyList(),
    isAnonymous: Boolean = true,
    lastLogin: Long = System.currentTimeMillis(),
    isActive: Boolean = true
): UserEntities = UserModel(
    id = id,
    likes = likes,
    isAnonymous = isAnonymous,
    lastLogin = lastLogin,
    isActive = isActive
)