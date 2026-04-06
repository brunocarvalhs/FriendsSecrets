package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.data.extensions.toVanillaMap
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
data class GroupDTO(
    @SerialName("id") val id: String,
    @SerialName("token") val token: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String?,
    @SerialName("date") val date: String?,
    @SerialName("min_price") val minPrice: Double?,
    @SerialName("max_price") val maxPrice: Double?,
    @SerialName("type") val type: String?,
    @SerialName("members") val members: List<UserDTO>,
    @SerialName("draws") val draws: Map<String, String>,
    @SerialName("is_owner") val isOwner: Boolean,
    @SerialName("photo_base64") val photo: String?
) {
    fun toDomain() = GroupModel(
        id = id,
        token = token,
        name = name,
        description = description,
        date = date,
        minPrice = minPrice,
        maxPrice = maxPrice,
        type = type,
        members = members.map { it.toDomain() },
        draws = draws,
        isOwner = isOwner,
        photo = photo
    )

    fun toMap(): Map<String, Any?> {
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
        return json.encodeToJsonElement(this).jsonObject.toVanillaMap()
    }

    companion object {
        fun fromDomain(model: GroupModel) = GroupDTO(
            id = model.id,
            token = model.token,
            name = model.name,
            description = model.description,
            date = model.date,
            minPrice = model.minPrice,
            maxPrice = model.maxPrice,
            type = model.type,
            members = model.members.map { UserDTO.fromDomain(it) },
            draws = model.draws,
            isOwner = model.isOwner,
            photo = model.photo
        )
    }
}
