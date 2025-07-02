package br.com.brunocarvalhs.friendssecrets.data.service

import br.com.brunocarvalhs.friendssecrets.domain.services.GenerativeService
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.perf.metrics.AddTrace
import dagger.Lazy
import javax.inject.Inject

class GenerativeServiceImpl @Inject constructor(
    private val generativeModel: Lazy<GenerativeModel>
) : GenerativeService {

    @AddTrace(name = "GenerativeServiceImpl.invoke", enabled = true)
    override suspend fun invoke(
        prompt: String,
    ): String? {
        val response = generativeModel.get().generateContent(prompt)
        return response.text
    }
}