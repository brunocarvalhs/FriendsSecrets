package br.com.brunocarvalhs.group.list.app.domain.entities

import kotlinx.serialization.Serializable
import android.util.Base64
import androidx.compose.runtime.Stable

@Stable
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
    val photoBase64: String? = null
) {
    val photo: Any?
        get() = try {
            if (!photoBase64.isNullOrBlank() && photoBase64.length > 100) {
                Base64.decode(photoBase64, Base64.NO_WRAP)
            } else null
        } catch (e: Exception) {
            null
        }

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
                isOwner = map["is_owner"] as? Boolean ?: false,
                createdAt = map["date"] as? String,
                type = map["type"] as? String,
                minPrice = (map["min_price"] as? Number)?.toInt(),
                maxPrice = (map["max_price"] as? Number)?.toInt(),
                photoBase64 = map["photo_base64"] as? String
            )
        }
    }
}
