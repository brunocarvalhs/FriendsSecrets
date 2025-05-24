package br.com.brunocarvalhs.friendssecrets.data.repository.dto

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.request.UserRequest
import br.com.brunocarvalhs.friendssecrets.domain.repository.response.UserResponse
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class UserDTO(
    @SerializedName(UserEntities.ID) override val id: String = UUID.randomUUID().toString(),
    @SerializedName(UserEntities.NAME) override val name: String,
    @SerializedName(UserEntities.PHOTO_URL) override val photoUrl: String? = null,
    @SerializedName(UserEntities.PHONE_NUMBER) override val phoneNumber: String = "",
    @SerializedName(UserEntities.IS_PHONE_NUMBER_VERIFIED) override val isPhoneNumberVerified: Boolean = false,
    @SerializedName(UserEntities.LIKES) override val likes: List<String> = emptyList(),
): UserRequest, UserResponse {

    override fun toMap(): Map<String, Any> {
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
