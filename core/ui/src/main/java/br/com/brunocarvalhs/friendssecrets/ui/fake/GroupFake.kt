package br.com.brunocarvalhs.friendssecrets.ui.fake

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

internal data class GroupFake(
    override val id: String,
    override val token: String,
    override val name: String,
    override val description: String?,
    override val members: List<UserEntities>,
    override val draws: Map<String, String>,
    override val isOwner: Boolean
) : GroupEntities {
    override fun toMap(): Map<String, Any?> {
        return emptyMap()
    }

    override fun toCopy(
        token: String,
        name: String,
        description: String?,
        members: List<UserEntities>,
        draws: Map<String, String>,
        isOwner: Boolean
    ): GroupEntities = this.copy()
}

fun GroupEntities.Companion.toFake(
    id: String = "1",
    token: String = "123456789",
    name: String = "Produto de Teste",
    description: String? = null,
    members: List<UserEntities> = emptyList(),
    draws: Map<String, String> = emptyMap(),
    isOwner: Boolean = false,
): GroupEntities = GroupFake(
    id = id,
    token = token,
    name = name,
    description = description,
    members = members,
    draws = draws,
    isOwner = isOwner
)

