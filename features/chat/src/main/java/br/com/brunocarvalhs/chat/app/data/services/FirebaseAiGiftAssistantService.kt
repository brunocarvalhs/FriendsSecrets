package br.com.brunocarvalhs.chat.app.data.services

import br.com.brunocarvalhs.chat.app.domain.services.AiChatSession
import br.com.brunocarvalhs.chat.app.domain.services.AiGiftAssistantService
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import javax.inject.Inject

class FirebaseAiGiftAssistantService @Inject constructor() : AiGiftAssistantService {

    override fun startChat(groupName: String): AiChatSession {
        val model = Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = MODEL_NAME,
            systemInstruction = content {
                text(
                    "You are a friendly, creative gift-idea assistant inside the Friends Secrets " +
                        "Secret Santa app. The user wants gift ideas for someone in their group " +
                        "\"$groupName\". Ask brief clarifying questions when helpful (hobbies, " +
                        "interests, budget, age) and suggest specific, actionable, varied gift " +
                        "ideas. Keep answers concise and always reply in the same language the " +
                        "user writes in."
                )
            }
        )
        return FirebaseAiChatSession(model.startChat())
    }

    private companion object {
        const val MODEL_NAME = "gemini-2.5-flash"
    }
}

private class FirebaseAiChatSession(
    private val chat: com.google.firebase.ai.Chat
) : AiChatSession {
    override suspend fun sendMessage(text: String): Result<String> = runCatching {
        chat.sendMessage(text).text.orEmpty()
    }
}
