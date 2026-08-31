package br.com.brunocarvalhs.core.notifications.data.model

import br.com.brunocarvalhs.core.domain.model.GroupModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GroupSyncDTO(
    @SerialName(GroupModel.TOKEN) val token: String = "",
    @SerialName(GroupModel.NAME) val name: String = "",
    @SerialName(GroupModel.DRAWS) val draws: Map<String, String> = emptyMap(),
)
