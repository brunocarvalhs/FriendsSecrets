package br.com.brunocarvalhs.friendssecrets.domain.repository.response

interface GroupResponse {
    val id: String
    val token: String
    val name: String
    val description: String?
    val members: Map<String, Map<String, String>>
    val draws: Map<String, String>
    val isOwner: Boolean
}