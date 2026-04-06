package br.com.brunocarvalhs.group.details.app.data.model

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDetailsDTO(
    @SerialName(GroupModel.ID) val id: String = "",
    @SerialName(GroupModel.NAME) val name: String = "",
    @SerialName(GroupModel.DESCRIPTION) val description: String? = null,
    @SerialName(GroupModel.TOKEN) val token: String = "",
    @SerialName(GroupModel.DATE) val date: String? = null,
    @SerialName(GroupModel.MIN_PRICE) val minPrice: Double? = null,
    @SerialName(GroupModel.MAX_PRICE) val maxPrice: Double? = null,
    @SerialName(GroupModel.TYPE) val type: String? = null,
    @SerialName(GroupModel.MEMBERS) val members: List<UserDetailsDTO> = emptyList(),
    @SerialName(GroupModel.DRAWS) val draws: Map<String, String> = emptyMap(),
    @SerialName(GroupModel.IS_OWNER) val isOwner: Boolean = false,
    @SerialName(GroupModel.PHOTO) val photo: String? = null
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
        isOwner = isOwner,
        photo = photo
    )
}
