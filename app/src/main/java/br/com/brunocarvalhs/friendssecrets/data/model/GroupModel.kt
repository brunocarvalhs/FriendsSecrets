package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.commons.extensions.token
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import com.google.firebase.perf.metrics.AddTrace
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.annotations.SerializedName
import java.util.UUID
import kotlin.random.Random

internal data class GroupModel(
    @SerializedName(GroupEntities.ID) override val id: String = UUID.randomUUID().toString(),
    @SerializedName(GroupEntities.TOKEN) override val token: String = Random.token(size = 8),
    @SerializedName(GroupEntities.NAME) override val name: String = "",
    @SerializedName(GroupEntities.DESCRIPTION) override val description: String? = null,
    @SerializedName(GroupEntities.MEMBERS) override val members: Map<String, String> = emptyMap(),
    @SerializedName(GroupEntities.DRAWS) override val draws: Map<String, String> = emptyMap(),
    @SerializedName(GroupEntities.IS_OWNER) override val isOwner: Boolean = false,
) : GroupEntities {

    @AddTrace(name = "GroupModel.toMap")
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

    @AddTrace(name = "GroupModel.toCopy")
    override fun toCopy(
        token: String,
        name: String,
        description: String?,
        members: Map<String, String>,
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
