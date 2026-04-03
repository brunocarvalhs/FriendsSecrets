package br.com.brunocarvalhs.group.app.domain.services

internal interface DrawService {
    fun drawMembers(participants: MutableList<String>): Map<String, String>
    fun revelation(code: String): String
}