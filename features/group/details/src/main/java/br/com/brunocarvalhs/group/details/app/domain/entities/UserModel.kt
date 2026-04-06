package br.com.brunocarvalhs.group.list.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val name: String,
    val likes: List<String>,
) {
    companion object {
        fun fromMap(map: Map<String, Any>): UserModel {
            return UserModel(
                name = map["name"] as? String ?: "",
                likes = (map["likes"] as? List<String> ?: emptyList()).toList(),
            )
        }
    }
}
