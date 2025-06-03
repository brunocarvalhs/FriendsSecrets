package br.com.brunocarvalhs.friendssecrets.data.service

import br.com.brunocarvalhs.friendssecrets.domain.services.GenerativeService
import com.google.ai.client.generativeai.GenerativeModel
import javax.inject.Inject

class GenerativeServiceImpl @Inject constructor(
    private val generativeModel: GenerativeModel
) : GenerativeService {
    override suspend fun invoke(
        prompt: String,
    ): String? {
        val response = generativeModel.generateContent(prompt)
        return response.text
    }
}