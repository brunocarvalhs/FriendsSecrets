package br.com.brunocarvalhs.group.details.app.data.model

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.data.constants.EMPTY_STRING
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GroupDetailsDTO(
    @SerialName(GroupModel.ID) val id: String = EMPTY_STRING,
    @SerialName(GroupModel.NAME) val name: String = EMPTY_STRING,
    @SerialName(GroupModel.DESCRIPTION) val description: String? = null,
    @SerialName(GroupModel.TOKEN) val token: String = EMPTY_STRING,
    @SerialName(GroupModel.DATE) val date: String? = null,
    @SerialName(GroupModel.MIN_PRICE) val minPrice: Double? = null,
    @SerialName(GroupModel.MAX_PRICE) val maxPrice: Double? = null,
    @SerialName(GroupModel.TYPE) val type: String? = null,
    @SerialName(GroupModel.PHOTO) val photoBase64: String? = null,
    @SerialName(GroupModel.MEMBERS) val members: List<UserDetailsDTO> = emptyList(),
    @SerialName(GroupModel.DRAWS) val draws: Map<String, String> = emptyMap(),
    @SerialName(GroupModel.OWNER_ID) val ownerId: String? = null,
    @SerialName(GroupModel.IS_OWNER) val isOwner: Boolean = false,
    @SerialName(GroupModel.CREATED_AT) val createdAt: Long = 0L
) {
    fun toDomain() = GroupModel(
        id = id,
        name = name,
        description = description,
        token = token,
        date = date,
        minPrice = minPrice,
        maxPrice = maxPrice,
        type = type,
        members = members.map { it.toDomain() },
        draws = draws,
        ownerId = ownerId,
        isOwner = isOwner,
        photo = photoBase64,
        createdAt = createdAt
    )
}
