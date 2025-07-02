package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.firebase.perf.metrics.AddTrace
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.UUID

internal data class UserModel(
    @SerializedName(UserEntities.ID) override val id: String = UUID.randomUUID().toString(),
    @SerializedName(UserEntities.NAME) override val name: String,
    @SerializedName(UserEntities.PHOTO_URL) override val photoUrl: String? = null,
    @SerializedName(UserEntities.PHONE_NUMBER) override val phoneNumber: String = "",
    @SerializedName(UserEntities.IS_PHONE_NUMBER_VERIFIED) override val isPhoneNumberVerified: Boolean = false,
    @SerializedName(UserEntities.LIKES) override val likes: List<String> = emptyList(),
    @SerializedName(UserEntities.IS_ANONYMOUS) override val isAnonymous: Boolean = false,
    @SerializedName(UserEntities.LAST_LOGIN) override val lastLogin: Long = System.currentTimeMillis(),
    @SerializedName(UserEntities.IS_ACTIVE) override val isActive: Boolean = true
) : UserEntities {

    @AddTrace(name = "UserModel.firstName", enabled = true)
    override fun firstName(): String {
        return name.split(" ").first()
    }

    @AddTrace(name = "UserModel.lastName", enabled = true)
    override fun lastName(): String {
        return name.split(" ").last()
    }

    @AddTrace(name = "UserModel.toMap", enabled = true)
    override fun toMap(): Map<String, Any> {
        return mapOf(
            UserEntities.ID to id,
            UserEntities.NAME to name,
            UserEntities.PHOTO_URL to photoUrl.orEmpty(),
            UserEntities.PHONE_NUMBER to phoneNumber,
            UserEntities.IS_PHONE_NUMBER_VERIFIED to isPhoneNumberVerified,
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
        name: String,
        photoUrl: String?,
        phoneNumber: String,
        isPhoneNumberVerified: Boolean,
        likes: List<String>,
        isAnonymous: Boolean,
        lastLogin: Long,
        isActive: Boolean
    ): UserEntities = copy(
        id = id,
        name = name,
        photoUrl = photoUrl,
        phoneNumber = phoneNumber,
        isPhoneNumberVerified = isPhoneNumberVerified,
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
                name = map[UserEntities.NAME] as? String ?: "",
                photoUrl = map[UserEntities.PHOTO_URL] as? String,
                phoneNumber = map[UserEntities.PHONE_NUMBER] as? String ?: "",
                isPhoneNumberVerified = map[UserEntities.IS_PHONE_NUMBER_VERIFIED] as? Boolean ?: false,
                likes = (map[UserEntities.LIKES] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                isAnonymous = map[UserEntities.IS_ANONYMOUS] as? Boolean ?: false,
                lastLogin = (map[UserEntities.LAST_LOGIN] as? Number)?.toLong() ?: System.currentTimeMillis(),
                isActive = map[UserEntities.IS_ACTIVE] as? Boolean ?: true
            )
        }
    }
}

@AddTrace(name = "UserEntities.create", enabled = true)
fun UserEntities.Companion.create(
    id: String = UUID.randomUUID().toString(),
    name: String = "",
    photoUrl: String? = null,
    phoneNumber: String = "",
    isPhoneNumberVerified: Boolean = false,
    likes: List<String> = emptyList(),
    isAnonymous: Boolean = false,
    lastLogin: Long = System.currentTimeMillis(),
    isActive: Boolean = true
): UserEntities = UserModel(
    id = id,
    name = name,
    photoUrl = photoUrl,
    phoneNumber = phoneNumber,
    isPhoneNumberVerified = isPhoneNumberVerified,
    likes = likes,
    isAnonymous = isAnonymous,
    lastLogin = lastLogin,
    isActive = isActive
)