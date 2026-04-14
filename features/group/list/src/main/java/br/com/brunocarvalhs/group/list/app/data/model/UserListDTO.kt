package br.com.brunocarvalhs.group.list.app.data.model

import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserListDTO(
    @SerialName(UserModel.Companion.ID) val id: String = "",
    @SerialName(UserModel.Companion.NAME) val name: String = "",
    @SerialName(UserModel.Companion.PHOTO_URL) val photoUrl: String? = null
) {
    fun toDomain() = UserModel(
        id = id,
        name = name,
        photoUrl = photoUrl
    )

    companion object {
        fun fromDomain(model: UserModel) = UserListDTO(
            id = model.id,
            name = model.name,
            photoUrl = model.photoUrl
        )
    }
}