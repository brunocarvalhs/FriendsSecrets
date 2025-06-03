package br.com.brunocarvalhs.friendssecrets.domain.services

interface DrawService {
    fun drawMembers(participants: MutableList<String>): Map<String, String>
    fun revelation(code: String): String
}