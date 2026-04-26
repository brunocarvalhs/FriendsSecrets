package br.com.brunocarvalhs.group.create.app.domain.model

import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.group.create.app.domain.constants.EMPTY_STRING

internal data class ContactModel(
    val id: String = EMPTY_STRING,
    val name: String = EMPTY_STRING,
    val phoneNumber: String = EMPTY_STRING,
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
