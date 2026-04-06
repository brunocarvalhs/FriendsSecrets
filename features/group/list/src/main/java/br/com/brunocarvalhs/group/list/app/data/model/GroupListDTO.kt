package br.com.brunocarvalhs.group.list.app.data.model

import br.com.brunocarvalhs.friendssecrets.domain.model.toVanillaMap
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.list.app.data.UserListDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
data class GroupListDTO(
    @SerialName(GroupModel.ID) val id: String = "",
    @SerialName(GroupModel.NAME) val name: String = "",
    @SerialName(GroupModel.DESCRIPTION) val description: String? = null,
    @SerialName(GroupModel.TOKEN) val token: String = "",
    @SerialName(GroupModel.DATE) val date: String? = null,
    @SerialName(GroupModel.PHOTO) val photo: String? = null,
    @SerialName(GroupModel.MEMBERS) val members: List<UserListDTO> = emptyList(),
    @SerialName(GroupModel.IS_OWNER) val isOwner: Boolean = false
) {
    fun toDomain() = GroupModel(
        id = id,
        name = name,
        description = description,
        token = token,
        date = date,
        photo = photo,
        members = members.map { it.toDomain() },
        isOwner = isOwner
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
            photo = model.photo,
            members = model.members.map { UserListDTO.fromDomain(it) },
            isOwner = model.isOwner
        )
    }
}
