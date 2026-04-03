package br.com.brunocarvalhs.group.app.data.extensions

import br.com.brunocarvalhs.group.app.data.model.GroupModel
import br.com.brunocarvalhs.group.app.data.model.UserModel
import br.com.brunocarvalhs.group.app.data.repositories.dto.GroupDTO
import com.google.firebase.perf.metrics.AddTrace
import java.util.UUID
import kotlin.random.Random

@AddTrace(name = "GroupEntities.create", enabled = true)
fun GroupEntities.Companion.create(
    id: String = UUID.randomUUID().toString(),
    token: String = token(),
    name: String = "",
    description: String? = null,
    date: String? = null,
    minPrice: Double? = null,
    maxPrice: Double? = null,
    type: String? = null,
    members: List<UserEntities> = emptyList(),
    draws: Map<String, String> = emptyMap(),
    isOwner: Boolean = false,
): GroupEntities = GroupModel(
    id = id,
    token = token,
    name = name,
    description = description,
    date = date,
    minPrice = minPrice,
    maxPrice = maxPrice,
    type = type,
    members = members.filterIsInstance<UserModel>(),
    draws = draws,
    isOwner = isOwner
)

@AddTrace(name = "Random.token", enabled = true)
internal fun token(size: Int = 8): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..size)
        .map { charPool[Random.nextInt(charPool.size)] }
        .joinToString("")
}

@AddTrace(name = "GroupEntities.toDTO", enabled = true)
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
        isOwner = this.isOwner,
        date = this.date,
        minPrice = this.minPrice,
        maxPrice = this.maxPrice,
        type = this.type
    )
}

@AddTrace(name = "GroupDTO.toEntities", enabled = true)
internal fun GroupDTO.toEntities(): GroupEntities {
    return GroupModel(
        id = this.id,
        token = this.token,
        name = this.name,
        description = this.description,
        members = this.members.values.mapNotNull { member ->
            runCatching {
                UserModel.fromMap(member) as UserModel
            }.getOrNull()
        },
        draws = this.draws,
        isOwner = this.isOwner,
        date = this.date,
        minPrice = this.minPrice,
        maxPrice = this.maxPrice,
        type = this.type
    )
}