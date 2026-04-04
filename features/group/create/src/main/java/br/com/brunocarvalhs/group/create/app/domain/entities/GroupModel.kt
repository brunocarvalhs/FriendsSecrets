package br.com.brunocarvalhs.group.create.app.domain.entities

import java.util.UUID
import kotlin.random.Random

data class GroupModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val members: List<ContactModel>,
    val token: String = token(),
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val date: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "description" to description,
            "members" to members.map { it.toMap() },
            "token" to token,
            "minPrice" to minPrice,
            "maxPrice" to maxPrice,
            "date" to date
        )
    }

    companion object {
        const val COLLECTION_NAME = "group_tokens"
        const val COLLECTION_NAME_ADMINS = "group_admins"

        fun token(size: Int = 8): String {
            val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            return (1..size)
                .map { charPool[Random.nextInt(charPool.size)] }
                .joinToString("")
        }
    }
}