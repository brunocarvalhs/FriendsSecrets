package br.com.brunocarvalhs.group.draw.commons.navigation

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class DrawGraphRouter(val group: GroupModel) {
    companion object {
        val typeMap = mapOf(
            typeOf<GroupModel>() to navTypeSerializer<GroupModel>()
        )
    }
}
