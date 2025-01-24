package br.com.brunocarvalhs.friendssecrets.domain.entities

interface UserEntities {
    val id: String
    val photo: String?
    val name: String
    val phone: String?
    val email: String?
    val likes: List<String>


    companion object {
        const val COLLECTION = "users"

        const val ID = "id"
        const val PHOTO = "photo"
        const val EMAIL = "email"
        const val NAME = "name"
        const val PHONE = "phone"
        const val LIKES = "nicks"
    }
}