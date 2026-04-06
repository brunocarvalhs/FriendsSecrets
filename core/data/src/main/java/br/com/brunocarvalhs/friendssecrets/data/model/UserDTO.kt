package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.data.extensions.toVanillaMap
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
data class UserDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("photoUrl") val photoUrl: String?,
    @SerialName("likes") val likes: List<String>
) {
    fun toDomain() = UserModel(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        photoUrl = photoUrl,
        likes = likes
    )

    fun toMap(): Map<String, Any?> {
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
        return json.encodeToJsonElement(this).jsonObject.toVanillaMap()
    }

    companion object {
        fun fromDomain(model: UserModel) = UserDTO(
            id = model.id,
            name = model.name,
            phoneNumber = model.phoneNumber,
            photoUrl = model.photoUrl,
            likes = model.likes
        )
    }
}
