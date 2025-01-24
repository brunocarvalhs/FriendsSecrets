package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName(UserEntities.ID) override val id: String,
    @SerializedName(UserEntities.PHOTO) override val photo: String?,
    @SerializedName(UserEntities.NAME) override val name: String,
    @SerializedName(UserEntities.EMAIL) override val email: String?,
    @SerializedName(UserEntities.PHONE) override val phone: String?,
    @SerializedName(UserEntities.LIKES) override val likes: List<String> = emptyList(),
) : UserEntities
