package br.com.brunocarvalhs.friendssecrets.ui.fake

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

internal data class UserFake(
    override val id: String = "1",
    override val name: String = "John Doe",
    override val photoUrl: String? = null,
    override val phoneNumber: String = "",
    override val isPhoneNumberVerified: Boolean = false,
    override val likes: List<String> = emptyList(),
    override val isAnonymous: Boolean = false,
    override val lastLogin: Long = System.currentTimeMillis(),
    override val isActive: Boolean = true
) : UserEntities {
    override fun toMap(): Map<String, Any> {
        return emptyMap()
    }

    override fun toJson(): String {
        return ""
    }

    override fun toCopy(
        id: String,
        name: String,
        photoUrl: String?,
        phoneNumber: String,
        isPhoneNumberVerified: Boolean,
        likes: List<String>,
        isAnonymous: Boolean,
        lastLogin: Long,
        isActive: Boolean
    ): UserEntities = this.copy()
}

fun UserEntities.Companion.toFake(
    id: String = "1",
    name: String = "John Doe",
    photoUrl: String? = null,
    phoneNumber: String = "",
    isPhoneNumberVerified: Boolean = false,
    likes: List<String> = emptyList(),
    isAnonymous: Boolean = false,
    lastLogin: Long = System.currentTimeMillis(),
    isActive: Boolean = true
): UserEntities = UserFake(
    id = id,
    name = name,
    photoUrl = photoUrl,
    phoneNumber = phoneNumber,
    isPhoneNumberVerified = isPhoneNumberVerified,
    likes = likes,
    isAnonymous = isAnonymous,
    lastLogin = lastLogin,
    isActive = isActive
)
