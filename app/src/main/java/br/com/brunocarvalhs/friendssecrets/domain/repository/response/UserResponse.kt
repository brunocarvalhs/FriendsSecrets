package br.com.brunocarvalhs.friendssecrets.domain.repository.response

import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

interface UserResponse {
    val id: String
    val name: String
    val photoUrl: String?
    val phoneNumber: String
    val isPhoneNumberVerified: Boolean
    val likes: List<String>
}

internal fun UserResponse.toModel(): UserEntities {
    return UserModel(
        id = id,
        name = name,
        photoUrl = photoUrl,
        phoneNumber = phoneNumber,
        isPhoneNumberVerified = isPhoneNumberVerified,
    )
}