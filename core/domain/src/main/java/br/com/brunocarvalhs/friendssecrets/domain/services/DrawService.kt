package br.com.brunocarvalhs.friendssecrets.domain.services

interface DrawService {
    fun draw(participants: MutableList<String>): Map<String, String>
}