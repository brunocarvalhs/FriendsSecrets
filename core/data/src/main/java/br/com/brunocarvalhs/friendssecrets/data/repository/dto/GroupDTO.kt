package br.com.brunocarvalhs.friendssecrets.data.repository.dto

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.UUID

internal data class GroupDTO(
    @SerializedName(GroupEntities.ID) val id: String = UUID.randomUUID().toString(),
    @SerializedName(GroupEntities.TOKEN) val token: String = "",
    @SerializedName(GroupEntities.NAME) val name: String = "",
    @SerializedName(GroupEntities.DESCRIPTION) val description: String? = null,
    @SerializedName(GroupEntities.MEMBERS) val members: Map<String, Map<String, Any>> = emptyMap(),
    @SerializedName(GroupEntities.DRAWS) val draws: Map<String, String> = emptyMap(),
    @SerializedName(GroupEntities.IS_OWNER) val isOwner: Boolean = false,
) {

    fun toMap(): Map<String, Any> {
        return mapOf(
            GroupEntities.ID to id,
            GroupEntities.TOKEN to token,
            GroupEntities.NAME to name,
            GroupEntities.DESCRIPTION to description.orEmpty(),
            GroupEntities.MEMBERS to members,
            GroupEntities.DRAWS to draws
        )
    }

    companion object {
        private val gson = Gson()

        fun fromMap(map: Map<String, Any>): GroupDTO {
            val json = gson.toJson(map)
            return gson.fromJson(json, GroupDTO::class.java)
        }
    }
}