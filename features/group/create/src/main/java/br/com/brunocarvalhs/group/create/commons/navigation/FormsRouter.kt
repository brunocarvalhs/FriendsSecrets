package br.com.brunocarvalhs.group.create.commons.navigation

import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel
import kotlinx.serialization.Serializable

@Serializable
internal data class FormsRouter(
    val members: List<ContactModel>,
)
