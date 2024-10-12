package br.com.brunocarvalhs.friendssecrets.data.service

import android.graphics.Bitmap
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

class GenerativeService(
    modelName: String = BuildConfig.MODEL_NAME,
    apiKey: String = BuildConfig.apiKey
) {

    private val generativeModel = GenerativeModel(
        modelName = modelName,
        apiKey = apiKey
    )

    suspend fun invoke(
        prompt: String,
        image: Bitmap? = null,
    ): String? {
        val response = generativeModel.generateContent(
            content {
                if (image != null) image(image)
                text(prompt)
            }
        )
        return response.text
    }
}