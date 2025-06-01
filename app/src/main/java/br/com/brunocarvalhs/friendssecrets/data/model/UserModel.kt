package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class UserModel(
    @SerializedName(UserEntities.ID) override val id: String = UUID.randomUUID().toString(),
    @SerializedName(UserEntities.NAME) override val name: String,
    @SerializedName(UserEntities.PHOTO_URL) override val photoUrl: String? = null,
    @SerializedName(UserEntities.PHONE_NUMBER) override val phoneNumber: String = "",
    @SerializedName(UserEntities.EMAIL) override val email: String = "",
    @SerializedName(UserEntities.IS_PHONE_NUMBER_VERIFIED) override val isPhoneNumberVerified: Boolean = false,
    @SerializedName(UserEntities.LIKES) override val likes: List<String> = emptyList(),
): UserEntities {

    override fun toMap(): Map<String, Any> {
        return mapOf(
            UserEntities.ID to id,
            UserEntities.NAME to name,
            UserEntities.PHOTO_URL to photoUrl.orEmpty(),
            UserEntities.PHONE_NUMBER to phoneNumber,
            UserEntities.IS_PHONE_NUMBER_VERIFIED to isPhoneNumberVerified,
            UserEntities.LIKES to (likes ?: emptyList())
        )
    }

    override fun toCopy(
        id: String,
        name: String,
        photoUrl: String?,
        phoneNumber: String,
        isPhoneNumberVerified: Boolean,
        likes: List<String>
    ): UserEntities = copy(
        id = id,
        name = name,
        photoUrl = photoUrl,
        phoneNumber = phoneNumber,
        isPhoneNumberVerified = isPhoneNumberVerified,
        likes = likes
    )

    companion object {
        private val gson = Gson()

        fun fromMap(map: Map<String, Any>): UserEntities {
            val json = gson.toJson(map)
            return gson.fromJson(json, UserModel::class.java)
        }
    }
}