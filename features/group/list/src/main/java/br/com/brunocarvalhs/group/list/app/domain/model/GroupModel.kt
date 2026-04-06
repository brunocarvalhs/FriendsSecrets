package br.com.brunocarvalhs.group.list.app.domain.model

import kotlinx.serialization.Serializable
import android.util.Base64
import androidx.compose.runtime.Stable
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

@Stable
@Serializable
data class GroupModel(
    override val name: String = "",
    override val description: String = "",
    override val members: List<UserModel> = emptyList(),
    override val draws: Map<String, String> = emptyMap(),
    override val isOwner: Boolean = false,
    override val type: String? = null,
    override val minPrice: Double? = null,
    override val maxPrice: Double? = null,
): GroupEntities() {

    override val photo: String?
        get() = try {
            if (!photo.isNullOrBlank() && photo?.length!! > 100) {
                Base64.decode(photo, Base64.NO_WRAP).toString(Charsets.UTF_8)
            } else null
        } catch (e: Exception) {
            null
        }

    override fun toCopy(
        token: String,
        name: String,
        description: String?,
        date: String?,
        minPrice: Double?,
        maxPrice: Double?,
        type: String?,
        members: List<UserEntities>,
        draws: Map<String, String>,
        isOwner: Boolean,
        photo: String?
    ): GroupEntities {
        return copy(isOwner = isOwner)
    }
}
