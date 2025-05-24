package br.com.brunocarvalhs.friendssecrets.domain.repository.response

interface UserResponse {
    val id: String
    val name: String
    val photoUrl: String?
    val phoneNumber: String
    val isPhoneNumberVerified: Boolean
    val likes: List<String>
}