package br.com.brunocarvalhs.friendssecrets.domain.repository.response

import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

interface GroupResponse {
    val id: String
    val token: String
    val name: String
    val description: String?
    val members: Map<String, Map<String, String>>
    val draws: Map<String, String>
    val isOwner: Boolean
}

internal fun GroupResponse.toModel(): GroupEntities {
    val membersList = members.map { (key, valueMap) ->
        UserModel(
            name = valueMap[UserEntities.NAME] ?: key,
            likes = valueMap[UserEntities.LIKES]?.split("|")?.map { it.trim() }.orEmpty(),
            photoUrl = valueMap[UserEntities.PHOTO_URL]?.takeIf { it.isNotEmpty() }
        )
    }

    return GroupModel(
        id = id,
        token = token,
        name = name,
        description = description,
        members = membersList,
        draws = draws,
        isOwner = isOwner
    )
}