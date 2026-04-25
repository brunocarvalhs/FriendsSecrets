package br.com.brunocarvalhs.friendssecrets.core.navigation.routers

import br.com.brunocarvalhs.friendssecrets.core.navigation.navtype.navTypeSerializer
import br.com.brunocarvalhs.friendssecrets.core.navigation.navtype.navTypeSerializerNullable
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data object GroupListGraph

@Serializable
data object GroupCreateGraph

@Serializable
data class GroupDetailsGraph(val group: GroupModel) {
    companion object {
        val typeMap = mapOf(
            typeOf<GroupModel>() to navTypeSerializer<GroupModel>()
        )
    }
}

@Serializable
data class EditFormsGraph(val group: GroupModel) {
    companion object {
        val typeMap = mapOf(
            typeOf<GroupModel>() to navTypeSerializer<GroupModel>()
        )
    }
}

@Serializable
data class ContactsRouter(
    val group: GroupModel? = null
) {
    companion object {
        val typeMap = mapOf(
            typeOf<GroupModel?>() to navTypeSerializerNullable<GroupModel>()
        )
    }
}

@Serializable
data class ChatGraph(
    val group: GroupModel,
) {
    companion object {
        val typeMap = mapOf(
            typeOf<GroupModel>() to navTypeSerializer<GroupModel>()
        )
    }
}

@Serializable
data class DrawGraph(val group: GroupModel) {
    companion object {
        val typeMap = mapOf(
            typeOf<GroupModel>() to navTypeSerializer<GroupModel>()
        )
    }
}

@Serializable
data object SettingsGraph

@Serializable
data object BiometricGraph
