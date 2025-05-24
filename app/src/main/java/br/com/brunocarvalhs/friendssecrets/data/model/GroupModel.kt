package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.commons.extensions.token
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.gson.Gson
import java.util.UUID
import kotlin.random.Random

internal data class GroupModel(
    override val id: String = UUID.randomUUID().toString(),
    override val token: String = Random.token(size = 8),
    override val name: String = "",
    override val description: String? = null,
    override val members: List<UserEntities> = emptyList(),
    override val draws: Map<String, String> = emptyMap(),
    override val isOwner: Boolean = false,
) : GroupEntities {

    override fun toCopy(
        token: String,
        name: String,
        description: String?,
        members: List<UserEntities>,
        draws: Map<String, String>,
        isOwner: Boolean,
    ): GroupEntities {
        return this.copy(
            token = token,
            name = name,
            description = description,
            members = members,
            draws = draws,
            isOwner = isOwner
        )
    }

    companion object {
        private val gson = Gson()

        fun fromMap(map: Map<String, Any>): GroupModel {
            val id = map[GroupEntities.ID] as? String ?: UUID.randomUUID().toString()
            val token = map[GroupEntities.TOKEN] as? String ?: ""
            val name = map[GroupEntities.NAME] as? String ?: ""
            val description = map[GroupEntities.DESCRIPTION] as? String
            val isOwner = map[GroupEntities.IS_OWNER] as? Boolean ?: false

            val draws = (map[GroupEntities.DRAWS] as? Map<*, *>)?.mapNotNull {
                val key = it.key as? String
                val value = it.value as? String
                if (key != null && value != null) key to value else null
            }?.toMap() ?: emptyMap()

            val membersMap = map[GroupEntities.MEMBERS] as? Map<*, *>
            val members = membersMap?.values?.mapNotNull { member ->
                try {
                    val memberMap = member as? Map<*, *>
                    val memberJson = gson.toJson(memberMap)
                    gson.fromJson(memberJson, UserModel::class.java)
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()

            return GroupModel(
                id = id,
                token = token,
                name = name,
                description = description,
                members = members,
                draws = draws,
                isOwner = isOwner
            )
        }
    }
}
