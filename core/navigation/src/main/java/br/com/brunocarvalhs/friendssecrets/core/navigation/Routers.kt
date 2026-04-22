package br.com.brunocarvalhs.friendssecrets.core.navigation

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data object GroupListGraph

@Serializable
data object GroupListRoute

@Serializable
data object GroupCreateGraph

@Serializable
data object GroupCreateRoute

@Serializable
data class GroupDetailsRoute(val group: GroupModel) {
    companion object {
        val typeMap = mapOf(
            typeOf<GroupModel>() to navTypeSerializer<GroupModel>()
        )
    }
}

@Serializable
data object SettingsGraph
