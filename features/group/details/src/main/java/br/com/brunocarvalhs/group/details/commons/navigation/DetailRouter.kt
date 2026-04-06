package br.com.brunocarvalhs.group.details.commons.navigation

import br.com.brunocarvalhs.group.details.app.domain.entities.GroupModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class DetailRouter(val groupModel: GroupModel) {
    companion object {
        val typeMap = mapOf(
            typeOf<GroupModel>() to navTypeSerializer<GroupModel>()
        )
    }
}
