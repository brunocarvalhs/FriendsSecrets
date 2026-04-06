package br.com.brunocarvalhs.group.details.commons.navigation

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import kotlinx.serialization.Serializable

@Serializable
data class GroupDetailsRouter(val group: GroupEntities)