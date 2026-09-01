package br.com.brunocarvalhs.core.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val phoneNumber: String = "",
    val photoUrl: String? = null,
    val likes: List<String> = emptyList()
) {
    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val PHONE_NUMBER = "phoneNumber"
        const val PHOTO_URL = "photoUrl"
        const val LIKES = "likes"
    }
}
