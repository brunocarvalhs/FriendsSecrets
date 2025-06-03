package br.com.brunocarvalhs.friendssecrets.data.mappers

import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.data.repository.dto.GroupDTO
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.gson.Gson

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

internal fun GroupDTO.toEntities(): GroupEntities {
    return GroupModel(
        id = this.id,
        token = this.token,
        name = this.name,
        description = this.description,
        members = this.members.values.mapNotNull { member ->
            try {
                val memberMap = member as? Map<*, *>
                val memberJson = Gson().toJson(memberMap)
                Gson().fromJson(memberJson, UserModel::class.java)
            } catch (e: Exception) {
                null
            }
        },
        draws = this.draws,
        isOwner = this.isOwner
    )
}