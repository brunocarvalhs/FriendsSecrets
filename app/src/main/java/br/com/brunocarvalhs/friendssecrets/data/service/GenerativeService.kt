package br.com.brunocarvalhs.friendssecrets.data.service

import br.com.brunocarvalhs.friendssecrets.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel

class GenerativeService(
    modelName: String = BuildConfig.MODEL_NAME,
    apiKey: String = BuildConfig.apiKey,
    private val generativeModel: GenerativeModel = GenerativeModel(
        modelName = modelName,
        apiKey = apiKey
    ),
) {
    suspend fun invoke(
        prompt: String,
    ): String? {
        val response = generativeModel.generateContent(prompt)
        return response.text
    }
}