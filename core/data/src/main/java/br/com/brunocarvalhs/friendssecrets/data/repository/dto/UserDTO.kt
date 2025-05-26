package br.com.brunocarvalhs.friendssecrets.data.repository.dto

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.UUID

internal data class UserDTO(
    @SerializedName(UserEntities.ID) val id: String = UUID.randomUUID().toString(),
    @SerializedName(UserEntities.NAME) val name: String,
    @SerializedName(UserEntities.PHOTO_URL) val photoUrl: String? = null,
    @SerializedName(UserEntities.PHONE_NUMBER) val phoneNumber: String = "",
    @SerializedName(UserEntities.IS_PHONE_NUMBER_VERIFIED) val isPhoneNumberVerified: Boolean = false,
    @SerializedName(UserEntities.LIKES) val likes: List<String> = emptyList(),
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            UserEntities.ID to id,
            UserEntities.NAME to name,
            UserEntities.PHOTO_URL to photoUrl.orEmpty(),
            UserEntities.PHONE_NUMBER to phoneNumber,
            UserEntities.IS_PHONE_NUMBER_VERIFIED to isPhoneNumberVerified,
            UserEntities.LIKES to likes
        )
    }

    companion object {
        private val gson = Gson()

        fun fromMap(map: Map<String, Any>): UserDTO {
            val json = gson.toJson(map)
            return gson.fromJson(json, UserDTO::class.java)
        }
    }
}
