package br.com.brunocarvalhs.group.create.app.data.model

import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel

data class ContactDTO(
    val id: String,
    val displayName: String,
    val phoneNumber: String? = null,
    val photoUri: String? = null,
    val email: String? = null,
    val address: String? = null,
    val company: String? = null,
    val jobTitle: String? = null,
    val birthday: String? = null
) {
    fun toDomain() = ContactModel(
        id = id,
        name = displayName,
        phoneNumber = phoneNumber.orEmpty(),
        photoUrl = photoUri,
        email = email,
        isSelected = false
    )
}
