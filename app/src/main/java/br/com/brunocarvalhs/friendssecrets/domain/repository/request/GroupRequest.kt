package br.com.brunocarvalhs.friendssecrets.domain.repository.request

interface GroupRequest {
    fun toMap(): Map<String, Any>
}