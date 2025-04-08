package br.com.brunocarvalhs.friendssecrets.domain.entities

interface UserEntities {
    val id: String
    val name: String
    val photoUrl: String?
    val phoneNumber: String
    val isPhoneNumberVerified: Boolean

    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val PHOTO_URL = "photoUrl"
        const val PHONE_NUMBER = "phoneNumber"
        const val IS_PHONE_NUMBER_VERIFIED = "isPhoneNumberVerified"
    }
}