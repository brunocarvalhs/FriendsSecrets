package br.com.brunocarvalhs.friendssecrets.domain.entities

interface UserEntities {
    val id: String
    val name: String
    val phoneNumber: String
    val photoUrl: String?
    val likes: List<String>

    fun toMap(): Map<String, Any>

    fun toJson(): String

    fun toCopy(
        id: String = this.id,
        likes: List<String> = this.likes,
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
