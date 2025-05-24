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
    val members: Map<String, Map<String, Any>>
    val draws: Map<String, String>
    val isOwner: Boolean
}

internal fun GroupResponse.toModel(): GroupEntities {
    val membersList = members.map { (key, valueMap) ->
        val rawPhotoUrl = valueMap[UserEntities.PHOTO_URL] as String?

        val cleanedPhotoUrl = rawPhotoUrl
            ?.takeIf { it.isNotEmpty() && !it.startsWith("content://com.android.contacts") }

        UserModel(
            name = (valueMap[UserEntities.NAME] ?: key) as String,
            likes = when (val likesValue = valueMap[UserEntities.LIKES]) {
                is String -> likesValue.split("|").map { it.trim() }
                is List<*> -> likesValue.filterIsInstance<String>()
                else -> emptyList()
            },
            photoUrl = cleanedPhotoUrl
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
