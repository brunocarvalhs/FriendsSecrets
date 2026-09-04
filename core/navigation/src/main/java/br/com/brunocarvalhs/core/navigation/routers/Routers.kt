package br.com.brunocarvalhs.core.navigation.routers

import br.com.brunocarvalhs.core.navigation.navtype.navTypeSerializer
import br.com.brunocarvalhs.core.navigation.navtype.navTypeSerializerNullable
import br.com.brunocarvalhs.core.domain.model.GroupModel
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
data class AiGiftChatGraph(
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
