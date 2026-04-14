package br.com.brunocarvalhs.group.draw.app.data.model

import br.com.brunocarvalhs.friendssecrets.domain.extensions.toVanillaMap
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
internal data class GroupDrawDTO(
    @SerialName(GroupModel.ID) val id: String,
    @SerialName(GroupModel.DRAWS) val draws: Map<String, String>,
) {
    fun toMap(): Map<String, Any?> {
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
        return json.encodeToJsonElement(this).jsonObject.toVanillaMap()
    }

    companion object {
        fun fromDomain(model: GroupModel) = GroupDrawDTO(
            id = model.id,
            draws = model.draws
        )
    }
}
