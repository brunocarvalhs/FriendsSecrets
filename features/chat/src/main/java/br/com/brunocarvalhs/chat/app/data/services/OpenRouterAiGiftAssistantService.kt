package br.com.brunocarvalhs.chat.app.data.services

import br.com.brunocarvalhs.chat.app.domain.services.AiChatSession
import br.com.brunocarvalhs.chat.app.domain.services.AiGiftAssistantService
import br.com.brunocarvalhs.core.domain.services.AiCredentialsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class OpenRouterAiGiftAssistantService @Inject constructor(
    private val httpClient: OkHttpClient,
    private val credentialsProvider: AiCredentialsProvider
) : AiGiftAssistantService {

    override fun startChat(groupName: String, membersContext: String): AiChatSession {
        val systemInstruction =
            "You are a friendly, creative gift-idea assistant inside the Friends Secrets " +
                "Secret Santa app. The user wants gift ideas for someone in their group " +
                "\"$groupName\". Here are the likes/interests each member has registered " +
                "in the app (use this as your primary basis for suggestions when the user " +
                "names one of these members; ask clarifying questions when helpful, e.g. " +
                "budget):\n" +
                membersContext + "\n\n" +
                "Suggest specific, actionable, varied gift ideas. Keep answers concise and " +
                "always reply in the same language the user writes in."

        return OpenRouterChatSession(
            httpClient = httpClient,
            apiKey = credentialsProvider.getOpenRouterApiKey(),
            systemInstruction = systemInstruction
        )
    }
}

private class OpenRouterChatSession(
    private val httpClient: OkHttpClient,
    private val apiKey: String,
    systemInstruction: String
) : AiChatSession {

    private val json = Json { ignoreUnknownKeys = true }
    private val history = mutableListOf(
        OpenRouterMessage(role = ROLE_SYSTEM, content = systemInstruction)
    )

    override suspend fun sendMessage(text: String): Result<String> =
        withContext(Dispatchers.IO) {
            runCatching {
                require(apiKey.isNotBlank()) { "Missing OpenRouter API key" }

                history.add(OpenRouterMessage(role = ROLE_USER, content = text))

                val requestBody = json.encodeToString(
                    OpenRouterChatRequest.serializer(),
                    OpenRouterChatRequest(model = MODEL_NAME, messages = history.toList())
                )

                val request = Request.Builder()
                    .url(CHAT_COMPLETIONS_URL)
                    .header("Authorization", "Bearer $apiKey")
                    .header("Content-Type", "application/json")
                    .header("HTTP-Referer", "https://github.com/brunocarvalhs/FriendsSecrets")
                    .header("X-OpenRouter-Title", "Friends Secrets")
                    .post(requestBody.toRequestBody("application/json".toMediaType()))
                    .build()

                httpClient.newCall(request).execute().use { response ->
                    val bodyString = response.body?.string().orEmpty()
                    check(response.isSuccessful) {
                        "OpenRouter request failed: ${response.code} $bodyString"
                    }

                    val parsed = json.decodeFromString(
                        OpenRouterChatResponse.serializer(),
                        bodyString
                    )
                    val reply = parsed.choices.firstOrNull()?.message?.content.orEmpty()
                    history.add(OpenRouterMessage(role = ROLE_ASSISTANT, content = reply))
                    reply
                }
            }
        }

    private companion object {
        const val CHAT_COMPLETIONS_URL = "https://openrouter.ai/api/v1/chat/completions"
        const val MODEL_NAME = "openrouter/free"
        const val ROLE_SYSTEM = "system"
        const val ROLE_USER = "user"
        const val ROLE_ASSISTANT = "assistant"
    }
}

@Serializable
private data class OpenRouterMessage(
    val role: String,
    val content: String
)

@Serializable
private data class OpenRouterChatRequest(
    val model: String,
    val messages: List<OpenRouterMessage>
)

@Serializable
private data class OpenRouterChatResponse(
    val choices: List<OpenRouterChoice> = emptyList()
)

@Serializable
private data class OpenRouterChoice(
    val message: OpenRouterMessage? = null
)
