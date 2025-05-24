package br.com.brunocarvalhs.friendssecrets.data.repository.dto

import br.com.brunocarvalhs.friendssecrets.commons.extensions.token
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.request.GroupRequest
import br.com.brunocarvalhs.friendssecrets.domain.repository.response.GroupResponse
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.UUID
import kotlin.random.Random

data class GroupDTO(
    @SerializedName(GroupEntities.ID) override val id: String = UUID.randomUUID().toString(),
    @SerializedName(GroupEntities.TOKEN) override val token: String = Random.token(size = 8),
    @SerializedName(GroupEntities.NAME) override val name: String = "",
    @SerializedName(GroupEntities.DESCRIPTION) override val description: String? = null,
    @SerializedName(GroupEntities.MEMBERS) override val members: Map<String, Map<String, Any>> = emptyMap(),
    @SerializedName(GroupEntities.DRAWS) override val draws: Map<String, String> = emptyMap(),
    @SerializedName(GroupEntities.IS_OWNER) override val isOwner: Boolean = false,
) : GroupResponse, GroupRequest {

    override fun toMap(): Map<String, Any> {
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

internal fun GroupEntities.toDTO(): GroupDTO {
    val membersMap = this.members.associate { user ->
        user.name to mapOf(
            GroupEntities.NAME to user.name,
            UserEntities.LIKES to user.likes,
            UserEntities.PHOTO_URL to (user.photoUrl ?: "")
        )
    }

    return GroupDTO(
        id = this.id,
        token = this.token,
        name = this.name,
        description = this.description,
        members = membersMap,
        draws = this.draws,
        isOwner = this.isOwner
    )
}