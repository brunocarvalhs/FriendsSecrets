package br.com.brunocarvalhs.friendssecrets.domain.services

interface GenerativeService {
    suspend fun invoke(prompt: String): String?
}