package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class UserModel(
    @SerializedName(UserEntities.ID) override val id: String = UUID.randomUUID().toString(),
    @SerializedName(UserEntities.PHOTO) override val photo: String? = null,
    @SerializedName(UserEntities.NAME) override val name: String = "",
    @SerializedName(UserEntities.EMAIL) override val email: String?= null,
    @SerializedName(UserEntities.PHONE) override val phone: String?= null,
    @SerializedName(UserEntities.LIKES) override val likes: List<String> = emptyList(),
    @SerializedName(UserEntities.GROUPS) override val groups: List<String> = emptyList(),
    @SerializedName(UserEntities.ADMIN_GROUPS) override val adminGroups: List<String> = emptyList(),
) : UserEntities {
    override fun toMap(): Map<*, *> {
        return mapOf(
            UserEntities.ID to id,
            UserEntities.PHOTO to photo,
            UserEntities.NAME to name,
            UserEntities.PHONE to phone,
            UserEntities.LIKES to likes,
        )
    }

    override fun firstName(): String = this.name.split(" ").first()

    override fun lastName(): String = this.name.split(" ").last()
}
