package br.com.brunocarvalhs.friendssecrets.domain.repository.request

interface UserRequest {
    fun toMap(): Map<String, Any>
}