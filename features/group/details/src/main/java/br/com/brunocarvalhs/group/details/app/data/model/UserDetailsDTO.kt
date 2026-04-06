package br.com.brunocarvalhs.group.details.app.data.model

import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDetailsDTO(
    @SerialName("id") val id: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("phoneNumber") val phoneNumber: String = "",
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("likes") val likes: List<String> = emptyList()
) {
    fun toDomain() = UserModel(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        photoUrl = photoUrl,
        likes = likes
    )
}