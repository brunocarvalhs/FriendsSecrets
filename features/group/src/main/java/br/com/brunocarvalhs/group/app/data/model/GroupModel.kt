package br.com.brunocarvalhs.group.app.data.model

import br.com.brunocarvalhs.group.app.data.extensions.token
import br.com.brunocarvalhs.group.app.domain.entities.GroupEntities
import br.com.brunocarvalhs.group.app.domain.entities.UserEntities
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.UUID

@Serializable
internal data class GroupModel(
    @SerialName(GroupEntities.ID) override val id: String = UUID.randomUUID().toString(),
    @SerialName(GroupEntities.TOKEN) override val token: String = token(size = 8),
    @SerialName(GroupEntities.NAME) override val name: String = "",
    @SerialName(GroupEntities.DESCRIPTION) override val description: String? = null,
    @SerialName(GroupEntities.MEMBERS) override val members: List<UserModel> = emptyList(),
    @SerialName(GroupEntities.DRAWS) override val draws: Map<String, String> = emptyMap(),
    @SerialName(GroupEntities.IS_OWNER) override val isOwner: Boolean = false,
    @SerialName(GroupEntities.DATE) override val date: String? = null,
    @SerialName(GroupEntities.MIN_PRICE) override val minPrice: Double? = null,
    @SerialName(GroupEntities.MAX_PRICE) override val maxPrice: Double? = null,
    @SerialName(GroupEntities.TYPE) override val type: String? = null
) : GroupEntities {

    @AddTrace(name = "GroupModel.toMap", enabled = true)
    override fun toMap(): Map<String, Any?> {
        return mapOf(
            GroupEntities.ID to id,
            GroupEntities.TOKEN to token,
            GroupEntities.NAME to name,
            GroupEntities.DESCRIPTION to description,
            GroupEntities.DATE to date,
            GroupEntities.MIN_PRICE to minPrice,
            GroupEntities.MAX_PRICE to maxPrice,
            GroupEntities.TYPE to type,
            GroupEntities.MEMBERS to members.map { it.toMap() },
            GroupEntities.DRAWS to draws,
            GroupEntities.IS_OWNER to isOwner
        )
    }

    @AddTrace(name = "GroupModel.toCopy", enabled = true)
    override fun toCopy(
        token: String,
        name: String,
        description: String?,
        date: String?,
        minPrice: Double?,
        maxPrice: Double?,
        type: String?,
        members: List<UserEntities>,
        draws: Map<String, String>,
        isOwner: Boolean
    ): GroupEntities {
        return this.copy(
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
    }

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        @AddTrace(name = "GroupModel.fromMap", enabled = true)
        fun fromMap(map: Map<String, Any>): GroupModel {
            val id = map[GroupEntities.ID] as? String ?: UUID.randomUUID().toString()
            val token = map[GroupEntities.TOKEN] as? String ?: ""
            val name = map[GroupEntities.NAME] as? String ?: ""
            val description = map[GroupEntities.DESCRIPTION] as? String
            val date = map[GroupEntities.DATE] as? String
            val minPrice = (map[GroupEntities.MIN_PRICE] as? Number)?.toDouble()
            val maxPrice = (map[GroupEntities.MAX_PRICE] as? Number)?.toDouble()
            val type = map[GroupEntities.TYPE] as? String
            val isOwner = map[GroupEntities.IS_OWNER] as? Boolean ?: false

            val draws = (map[GroupEntities.DRAWS] as? Map<*, *>)?.mapNotNull {
                val key = it.key as? String
                val value = it.value as? String
                if (key != null && value != null) key to value else null
            }?.toMap() ?: emptyMap()

            val membersMap = map[GroupEntities.MEMBERS] as? Map<*, *>
            val members = membersMap?.values?.mapNotNull { member ->
                try {
                    val memberMap = member as? Map<String, Any?>
                    if (memberMap != null) {
                        UserModel.fromMap(memberMap) as? UserModel
                    } else null
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()

            return GroupModel(
                id = id,
                token = token,
                name = name,
                description = description,
                date = date,
                minPrice = minPrice,
                maxPrice = maxPrice,
                type = type,
                members = members,
                draws = draws,
                isOwner = isOwner
            )
        }
    }
}

