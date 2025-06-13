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
    @SerializedName(GroupEntities.DATE) val date: String? = null,
    @SerializedName(GroupEntities.MIN_PRICE) val minPrice: Double? = null,
    @SerializedName(GroupEntities.MAX_PRICE) val maxPrice: Double? = null,
    @SerializedName(GroupEntities.TYPE) val type: String? = null
) {

    fun toMap(): Map<String, Any> {
        val result = mutableMapOf<String, Any>()

        result[GroupEntities.ID] = id
        result[GroupEntities.TOKEN] = token
        result[GroupEntities.NAME] = name
        result[GroupEntities.MEMBERS] = members
        result[GroupEntities.DRAWS] = draws

        description?.let { result[GroupEntities.DESCRIPTION] = it }
        date?.let { result[GroupEntities.DATE] = it }
        minPrice?.let { result[GroupEntities.MIN_PRICE] = it }
        maxPrice?.let { result[GroupEntities.MAX_PRICE] = it }
        type?.let { result[GroupEntities.TYPE] = it }

        return result
    }

    companion object {
        private val gson = Gson()

        fun fromMap(map: Map<String, Any>): GroupDTO {
            val json = gson.toJson(map)
            return gson.fromJson(json, GroupDTO::class.java)
        }
    }
}