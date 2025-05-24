package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.commons.extensions.token
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.annotations.SerializedName
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
            val json = gson.toJson(map)
            return gson.fromJson(json, GroupModel::class.java)
        }
    }
}
