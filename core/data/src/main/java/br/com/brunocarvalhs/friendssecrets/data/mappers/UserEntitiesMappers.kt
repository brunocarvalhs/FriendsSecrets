package br.com.brunocarvalhs.friendssecrets.data.mappers

import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.data.repository.dto.UserDTO
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

internal fun UserDTO.toEntities(): UserEntities {
    return UserModel(
        id = this.id,
        name = this.name,
        photoUrl = this.photoUrl,
        phoneNumber = this.phoneNumber,
        isPhoneNumberVerified = this.isPhoneNumberVerified,
        likes = this.likes
    )
}

fun UserEntities.factory(
    id: String = this.id,
    name: String = this.name,
    photoUrl: String? = this.photoUrl,
    phoneNumber: String = this.phoneNumber,
    isPhoneNumberVerified: Boolean = this.isPhoneNumberVerified,
    likes: List<String> = this.likes
): UserEntities = UserModel(id, name, photoUrl, phoneNumber, isPhoneNumberVerified, likes)