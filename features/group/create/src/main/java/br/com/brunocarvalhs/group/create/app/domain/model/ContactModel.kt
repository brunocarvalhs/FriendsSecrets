package br.com.brunocarvalhs.group.create.app.domain.model

import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel

data class ContactModel(
    val id: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val photoUrl: String? = null,
    val email: String? = null,
    val isSelected: Boolean = false
) {
    fun toUserModel() = UserModel(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        photoUrl = photoUrl,
        likes = emptyList()
    )
}
