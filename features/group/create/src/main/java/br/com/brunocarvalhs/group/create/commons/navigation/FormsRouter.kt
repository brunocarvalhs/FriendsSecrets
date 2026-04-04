package br.com.brunocarvalhs.group.create.commons.navigation

import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
internal data class FormsRouter(
    val members: List<ContactModel>,
    val contacts: Int
) {
    companion object {
        val typeMap = mapOf(
            typeOf<List<ContactModel>>() to navTypeListSerializer<ContactModel>()
        )
    }
}
