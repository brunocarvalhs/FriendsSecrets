package br.com.brunocarvalhs.group.create.app.domain.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities.Companion.LIKES
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities.Companion.NAME
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities.Companion.PHONE_NUMBER
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities.Companion.PHOTO_URL
import kotlinx.serialization.Serializable

@Serializable
data class ContactModel(
    override val photoUrl: String? = null,
    override val name: String = "",
    override val phoneNumber: String = "",
) : UserEntities() {

    override fun toMap(): Map<String, Any?> {
        return mapOf(
            PHOTO_URL to photoUrl,
            NAME to name,
            PHONE_NUMBER to phoneNumber,
            LIKES to likes
        )
    }

    override fun toCopy(
        photoUrl: String?,
        name: String,
        phoneNumber: String,
        likes: List<String>
    ): UserEntities {
        return this.copy(
            photoUrl = photoUrl,
            name = name,
            phoneNumber = phoneNumber,
        )
    }
}