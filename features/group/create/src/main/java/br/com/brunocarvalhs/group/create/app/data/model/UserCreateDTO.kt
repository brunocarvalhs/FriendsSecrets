package br.com.brunocarvalhs.group.create.app.data.model

import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserCreateDTO(
    @SerialName(UserModel.ID) val id: String,
    @SerialName(UserModel.NAME) val name: String,
    @SerialName(UserModel.PHONE_NUMBER) val phoneNumber: String,
    @SerialName(UserModel.PHOTO_URL) val photoUrl: String?,
    @SerialName(UserModel.LIKES) val likes: List<String>
) {
    companion object {
        fun fromDomain(model: UserModel) = UserCreateDTO(
            id = model.id,
            name = model.name,
            phoneNumber = model.phoneNumber,
            photoUrl = model.photoUrl,
            likes = model.likes
        )
    }
}
