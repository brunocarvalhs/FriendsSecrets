package br.com.brunocarvalhs.friendssecrets.ui.fake

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

internal data class UserFake(
    override val id: String = "1",
    override val name: String = "John Doe",
    override val photoUrl: String? = null,
    override val phoneNumber: String = "",
    override val isPhoneNumberVerified: Boolean = false,
    override val likes: List<String> = emptyList()
) : UserEntities {
    override fun toMap(): Map<String, Any> {
        return emptyMap()
    }

    override fun toCopy(
        id: String,
        name: String,
        photoUrl: String?,
        phoneNumber: String,
        isPhoneNumberVerified: Boolean,
        likes: List<String>
    ): UserEntities = this.copy()
}

internal fun UserEntities.Companion.toFake(
    id: String = "1",
    name: String = "John Doe",
    photoUrl: String? = null,
    phoneNumber: String = "",
    isPhoneNumberVerified: Boolean = false,
    likes: List<String> = emptyList()
) = UserFake(
    id = id,
    name = name,
    photoUrl = photoUrl,
    phoneNumber = phoneNumber,
    isPhoneNumberVerified = isPhoneNumberVerified,
    likes = likes
)
