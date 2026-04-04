package br.com.brunocarvalhs.group.list.app.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class GroupModel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val token: String = "",
    val members: List<UserModel> = emptyList(),
    val draws: Map<String, String> = emptyMap(),
    val isOwner: Boolean = false,
    val createdAt: String? = null,
    val type: String? = null,
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
) {

    fun toCopy(isOwner: Boolean): GroupModel {
        return copy(isOwner = isOwner)
    }

    companion object {
        const val COLLECTION_NAME = "group_tokens"
        const val COLLECTION_NAME_ADMINS = "group_admins"
        const val STORAGE_KEY = "group_tokens"

        fun fromMap(map: Map<String, Any>): GroupModel {
            return GroupModel(
                id = map["id"] as? String ?: "",
                name = map["name"] as? String ?: "",
                description = map["description"] as? String ?: "",
                token = map["token"] as? String ?: "",
                members = (map["members"] as? List<Map<String, Any>>)?.map { UserModel.fromMap(it) } ?: emptyList(),
                draws = (map["draws"] as? Map<String, String>) ?: emptyMap(),
                isOwner = map["isOwner"] as? Boolean ?: false,
                createdAt = map["createdAt"] as? String,
                type = map["type"] as? String,
                minPrice = (map["minPrice"] as? Number)?.toInt(),
                maxPrice = (map["maxPrice"] as? Number)?.toInt(),
            )
        }
    }
}
