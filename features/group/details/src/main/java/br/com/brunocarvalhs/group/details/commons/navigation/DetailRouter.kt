package br.com.brunocarvalhs.group.details.commons.navigation

import br.com.brunocarvalhs.core.navigation.navtype.navTypeSerializer
import br.com.brunocarvalhs.core.domain.model.GroupModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
internal data class DetailRouter(val group: GroupModel) {
    companion object {
        val typeMap = mapOf(
            typeOf<GroupModel>() to navTypeSerializer<GroupModel>()
        )
    }
}
