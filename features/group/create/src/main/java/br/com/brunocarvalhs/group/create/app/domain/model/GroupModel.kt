package br.com.brunocarvalhs.group.create.app.domain.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

data class GroupModel(
    override val name: String,
    override val description: String? = null,
    override val members: List<UserEntities> = emptyList(),
    override val minPrice: Double? = null,
    override val maxPrice: Double? = null,
    override val date: String? = null,
    override val photo: String? = null,
    override val type: String? = null,
    override val draws: Map<String, String> = mapOf(),
    override val isOwner: Boolean = true
): GroupEntities() {

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
        isOwner: Boolean,
        photo: String?
    ): GroupEntities {
        return this.copy(
            name = name,
            description = description,
            date = date,
            minPrice = minPrice,
            maxPrice = maxPrice,
            type = type,
            draws = draws,
            isOwner = isOwner,
            photo = photo
        )
    }
}