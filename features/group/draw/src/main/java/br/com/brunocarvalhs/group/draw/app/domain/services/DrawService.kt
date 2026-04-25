package br.com.brunocarvalhs.group.draw.app.domain.services

interface DrawService {
    fun draw(participants: MutableList<String>): Map<String, String>
}
