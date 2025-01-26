package br.com.brunocarvalhs.friendssecrets.domain.entities

interface UserEntities {
    val id: String
    val photo: String?
    val name: String
    val phone: String?
    val email: String?
    val groups: List<String>
    val adminGroups: List<String>
    val likes: List<String>

    fun toMap(): Map<*, *>

    fun firstName(): String

    fun lastName(): String

    companion object {
        const val COLLECTION = "users"

        const val ID = "id"
        const val PHOTO = "photo"
        const val EMAIL = "email"
        const val NAME = "name"
        const val PHONE = "phone"
        const val LIKES = "likes"
        const val GROUPS = "groups"
        const val ADMIN_GROUPS = "admin_groups"
    }
}