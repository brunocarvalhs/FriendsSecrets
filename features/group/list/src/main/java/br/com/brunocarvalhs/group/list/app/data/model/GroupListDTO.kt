package br.com.brunocarvalhs.group.list.app.data.model

import br.com.brunocarvalhs.friendssecrets.domain.extensions.toVanillaMap
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
internal data class GroupListDTO(
    @SerialName(GroupModel.ID) val id: String = "",
    @SerialName(GroupModel.NAME) val name: String = "",
    @SerialName(GroupModel.DESCRIPTION) val description: String? = null,
    @SerialName(GroupModel.TOKEN) val token: String = "",
    @SerialName(GroupModel.DATE) val date: String? = null,
    @SerialName(GroupModel.MIN_PRICE) val minPrice: Double? = null,
    @SerialName(GroupModel.MAX_PRICE) val maxPrice: Double? = null,
    @SerialName(GroupModel.TYPE) val type: String? = null,
    @SerialName(GroupModel.PHOTO) val photoBase64: String? = null,
    @SerialName(GroupModel.MEMBERS) val members: List<UserListDTO> = emptyList(),
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
        photo = photoBase64,
        members = members.map { it.toDomain() },
        ownerId = ownerId,
        draws = draws,
        isOwner = isOwner,
        createdAt = createdAt
    )

    fun toMap(): Map<String, Any?> {
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
        return json.encodeToJsonElement(this).jsonObject.toVanillaMap()
    }

    companion object {
        fun fromDomain(model: GroupModel) = GroupListDTO(
            id = model.id,
            name = model.name,
            description = model.description,
            token = model.token,
            date = model.date,
            photoBase64 = model.photo,
            minPrice = model.minPrice,
            maxPrice = model.maxPrice,
            type = model.type,
            members = model.members.map { UserListDTO.fromDomain(it) },
            ownerId = model.ownerId,
            draws = model.draws,
            isOwner = model.isOwner,
            createdAt = model.createdAt
        )
    }
}
