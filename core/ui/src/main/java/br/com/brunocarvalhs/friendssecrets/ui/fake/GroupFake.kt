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
    override val isOwner: Boolean,
    override val date: String? = null,
    override val minPrice: Double? = null,
    override val maxPrice: Double? = null,
    override val type: String? = null
) : GroupEntities {
    override fun toMap(): Map<String, Any?> {
        return emptyMap()
    }

    override fun toCopy(
        token: String,
        name: String,
        description: String?,
        date: String?,
        minPrice: Double?,
        maxPrice: Double?,
        type: String?,
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
    date: String? = null,
    minPrice: Double? = null,
    maxPrice: Double? = null,
    type: String? = null
): GroupEntities = GroupFake(
    id = id,
    token = token,
    name = name,
    description = description,
    members = members,
    draws = draws,
    isOwner = isOwner,
    date = date,
    minPrice = minPrice,
    maxPrice = maxPrice,
    type = type
)

