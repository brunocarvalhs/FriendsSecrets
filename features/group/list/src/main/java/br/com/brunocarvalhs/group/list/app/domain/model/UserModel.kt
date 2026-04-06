package br.com.brunocarvalhs.group.list.app.domain.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    override val name: String,
    override val likes: List<String>,
): UserEntities() {

    override fun toCopy(
        photoUrl: String?,
        name: String,
        phoneNumber: String,
        likes: List<String>
    ): UserEntities {
        return this.copy(
            name = name,
            likes = likes
        )
    }
}
