package br.com.brunocarvalhs.friendssecrets.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.random.Random

@Serializable
data class GroupModel(
    val id: String = UUID.randomUUID().toString(),
    val token: String = generateToken(),
    val name: String = "",
    val description: String? = null,
    val date: String? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val type: String? = null,
    val members: List<UserModel> = emptyList(),
    val draws: Map<String, String> = emptyMap(),
    val isOwner: Boolean = false,
    val ownerId: String? = null,
    val photo: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
) {
    companion object {
        const val COLLECTION_NAME = "groups"
        const val COLLECTION_NAME_ADMIN = "admins"

        const val ID = "id"
        const val TOKEN = "token"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val DATE = "date"
        const val MIN_PRICE = "min_price"
        const val MAX_PRICE = "max_price"
        const val TYPE = "type"
        const val MEMBERS = "members"
        const val DRAWS = "draws"
        const val IS_OWNER = "is_owner"
        const val OWNER_ID = "owner_id"
        const val PHOTO = "photo_base64"
        const val CREATED_AT = "created_at"

        fun generateToken(size: Int = 8): String {
            val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            return (1..size)
                .map { charPool[Random.nextInt(charPool.size)] }
                .joinToString("")
        }
    }
}
