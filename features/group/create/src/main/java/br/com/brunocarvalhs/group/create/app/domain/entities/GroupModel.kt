package br.com.brunocarvalhs.group.create.app.domain.entities

import java.util.UUID
import kotlin.random.Random

data class GroupModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val members: List<ContactModel>,
    val token: String = token()
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "members" to members.map { it.toMap() },
            "token" to token
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