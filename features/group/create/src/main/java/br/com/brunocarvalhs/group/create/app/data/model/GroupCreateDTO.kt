package br.com.brunocarvalhs.group.create.app.data.model

import br.com.brunocarvalhs.friendssecrets.domain.model.toVanillaMap
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
data class GroupCreateDTO(
    @SerialName(GroupModel.ID) val id: String,
    @SerialName(GroupModel.NAME) val name: String,
    @SerialName(GroupModel.DESCRIPTION) val description: String?,
    @SerialName(GroupModel.TOKEN) val token: String,
    @SerialName(GroupModel.DATE) val date: String?,
    @SerialName(GroupModel.MIN_PRICE) val minPrice: Double?,
    @SerialName(GroupModel.MAX_PRICE) val maxPrice: Double?,
    @SerialName(GroupModel.TYPE) val type: String?,
    @SerialName(GroupModel.MEMBERS) val members: List<UserCreateDTO>,
    @SerialName(GroupModel.DRAWS) val draws: Map<String, String>,
    @SerialName(GroupModel.IS_OWNER) val isOwner: Boolean,
    @SerialName(GroupModel.PHOTO) val photo: String?,
    @SerialName(GroupModel.CREATED_AT) val createdAt: Long
) {
    fun toMap(): Map<String, Any?> {
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
        return json.encodeToJsonElement(this).jsonObject.toVanillaMap()
    }

    companion object {
        fun fromDomain(model: GroupModel) = GroupCreateDTO(
            id = model.id,
            name = model.name,
            description = model.description,
            token = model.token,
            date = model.date,
            minPrice = model.minPrice,
            maxPrice = model.maxPrice,
            type = model.type,
            members = model.members.map { UserCreateDTO.fromDomain(it) },
            draws = model.draws,
            isOwner = model.isOwner,
            photo = model.photo,
            createdAt = model.createdAt
        )
    }
}
