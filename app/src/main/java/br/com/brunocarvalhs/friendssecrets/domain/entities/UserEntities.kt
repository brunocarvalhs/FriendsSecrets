package br.com.brunocarvalhs.friendssecrets.domain.entities

interface UserEntities {
    val id: String
    val name: String
    val photoUrl: String?
    val phoneNumber: String
    val isPhoneNumberVerified: Boolean
    val likes: List<String>

    fun toMap(): Map<String, Any>

    fun toCopy(
        id: String = this.id,
        name: String = this.name,
        photoUrl: String? = this.photoUrl,
        phoneNumber: String = this.phoneNumber,
        isPhoneNumberVerified: Boolean = this.isPhoneNumberVerified,
        likes: List<String> = this.likes
    ): UserEntities

    companion object {
        const val COLLECTION_NAME = "users"

        const val ID = "id"
        const val NAME = "name"
        const val PHOTO_URL = "photoUrl"
        const val PHONE_NUMBER = "phoneNumber"
        const val IS_PHONE_NUMBER_VERIFIED = "isPhoneNumberVerified"
        const val LIKES = "likes"
    }
}

fun List<UserEntities>.byName(name: String): UserEntities? = find { it.name == name }

fun List<UserEntities>.byPhoneNumber(phoneNumber: String): UserEntities? =
    find { it.phoneNumber == phoneNumber }

fun String.toLikes(): List<String> = split("|")