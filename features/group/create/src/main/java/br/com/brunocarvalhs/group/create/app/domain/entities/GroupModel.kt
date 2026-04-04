package br.com.brunocarvalhs.group.create.app.domain.entities

import java.util.UUID

data class GroupModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val members: List<ContactModel>,
    val token: String = UUID.randomUUID().toString().substring(0, 6).uppercase()
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "members" to members.map { it.toMap() },
            "token" to token
        )
    }
}