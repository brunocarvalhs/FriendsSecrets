package br.com.brunocarvalhs.group.create.commons.navigation

import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
internal data class FormsRouter(
    val members: List<UserModel>,
) {
    companion object {
        val typeMap = mapOf(
            typeOf<List<UserModel>>() to navTypeListSerializer<UserModel>()
        )
    }
}
