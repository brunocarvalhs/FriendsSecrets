package br.com.brunocarvalhs.friendssecrets.domain.entities

import java.util.UUID

abstract class UserEntities {
    open val id: String = UUID.randomUUID().toString()
    open val name: String = ""
    open val phoneNumber: String = ""
    open val photoUrl: String? = null
    open val likes: List<String> = emptyList()

    open fun toMap(): Map<String, Any?> = mapOf(
        ID to id,
        NAME to name,
        PHONE_NUMBER to phoneNumber,
        PHOTO_URL to photoUrl,
        LIKES to likes
    )

    abstract fun toCopy(
        photoUrl: String? = this.photoUrl,
        name: String = this.name,
        phoneNumber: String = this.phoneNumber,
        likes: List<String> = this.likes
    ): UserEntities

    companion object {
        const val COLLECTION_NAME = "users"

        const val ID = "id"
        const val LIKES = "likes"
        const val NAME = "name"
        const val PHOTO_URL = "photoUrl"
        const val PHONE_NUMBER = "phoneNumber"
    }
}

