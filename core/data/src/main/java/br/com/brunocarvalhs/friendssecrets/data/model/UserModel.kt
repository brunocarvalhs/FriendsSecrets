package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
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

        fun fromMap(map: Map<String, Any>): UserEntities {
            val json = gson.toJson(map)
            return gson.fromJson(json, UserModel::class.java)
        }
    }
}

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