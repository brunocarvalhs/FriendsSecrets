package br.com.brunocarvalhs.group.details.app.data.model

import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.group.details.app.data.constants.EMPTY_STRING
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserDetailsDTO(
    @SerialName("id") val id: String = EMPTY_STRING,
    @SerialName("name") val name: String = EMPTY_STRING,
    @SerialName("phoneNumber") val phoneNumber: String = EMPTY_STRING,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("likes") val likes: List<String> = emptyList(),
    @SerialName("adjectives") val adjectives: Map<String, List<String>> = emptyMap()
) {
    fun toDomain() = UserModel(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        photoUrl = photoUrl,
        likes = likes,
        adjectives = adjectives
    )

    companion object {
        fun fromDomain(model: UserModel) = UserDetailsDTO(
            id = model.id,
            name = model.name,
            phoneNumber = model.phoneNumber,
            photoUrl = model.photoUrl,
            likes = model.likes,
            adjectives = model.adjectives
        )
    }
}
