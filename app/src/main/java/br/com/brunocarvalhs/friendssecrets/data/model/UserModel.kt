package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class UserModel(
    @SerializedName(UserEntities.ID) override val id: String = UUID.randomUUID().toString(),
    @SerializedName(UserEntities.NAME) override val name: String,
    @SerializedName(UserEntities.PHOTO_URL) override val photoUrl: String?,
    @SerializedName(UserEntities.PHONE_NUMBER) override val phoneNumber: String,
    @SerializedName(UserEntities.IS_PHONE_NUMBER_VERIFIED) override val isPhoneNumberVerified: Boolean,
): UserEntities