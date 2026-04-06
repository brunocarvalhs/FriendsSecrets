package br.com.brunocarvalhs.group.details.commons.navigation

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class GroupDetailsRouter(val group: GroupModel) {
    companion object {
        val typeMap = mapOf(
            typeOf<GroupModel>() to navTypeSerializer<GroupModel>()
        )
    }
}