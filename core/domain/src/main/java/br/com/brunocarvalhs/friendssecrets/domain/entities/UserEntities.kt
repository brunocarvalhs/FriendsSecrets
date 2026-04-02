package br.com.brunocarvalhs.friendssecrets.domain.entities

/**
 * User entity - Anonymous user without personal data
 * Note: All fields are intentionally minimal to ensure user privacy.
 * No PII (Personally Identifiable Information) like name, phone, or photos should be stored.
 */
interface UserEntities {
    val id: String                          // UUID unique identifier (anonymous)
    val likes: List<String>                 // User preferences
    val isAnonymous: Boolean                // Always true
    val lastLogin: Long                     // For analytics
    val isActive: Boolean                   // Account status

    fun toMap(): Map<String, Any>

    fun toJson(): String

    fun toCopy(
        id: String = this.id,
        likes: List<String> = this.likes,
        isAnonymous: Boolean = this.isAnonymous,
        lastLogin: Long = this.lastLogin,
        isActive: Boolean = this.isActive
    ): UserEntities

    companion object {
        const val COLLECTION_NAME = "users"

        const val ID = "id"
        const val LIKES = "likes"
        const val IS_ANONYMOUS = "isAnonymous"
        const val LAST_LOGIN = "lastLogin"
        const val IS_ACTIVE = "isActive"

        // Deprecated - removed for privacy
        @Deprecated("Use anonymous users only. No PII should be stored.")
        const val NAME = "name"
        @Deprecated("Use anonymous users only. No PII should be stored.")
        const val PHOTO_URL = "photoUrl"
        @Deprecated("Use anonymous users only. No PII should be stored.")
        const val PHONE_NUMBER = "phoneNumber"
        @Deprecated("Use anonymous users only. No PII should be stored.")
        const val IS_PHONE_NUMBER_VERIFIED = "isPhoneNumberVerified"
    }
}