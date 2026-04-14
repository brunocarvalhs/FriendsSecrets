package br.com.brunocarvalhs.group.details.app.data.model

import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.details.app.data.constants.EMPTY_STRING
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserDetailsDTO(
    @SerialName("id") val id: String = EMPTY_STRING,
    @SerialName("name") val name: String = EMPTY_STRING,
    @SerialName("phoneNumber") val phoneNumber: String = EMPTY_STRING,
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