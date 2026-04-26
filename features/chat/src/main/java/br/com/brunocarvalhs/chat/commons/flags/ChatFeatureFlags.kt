package br.com.brunocarvalhs.chat.commons.flags

import br.com.brunocarvalhs.core.remote.domain.FeatureFlagService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ChatFeatureFlags @Inject constructor(
    private val service: FeatureFlagService
) {
    fun isChatEnabled(): Boolean = service.validate(FEATURE_CHAT, true)
    fun isSendMessageEnabled(): Boolean = service.validate(FEATURE_CHAT_SEND_MESSAGE, true)
}

private const val FEATURE_CHAT = "feature_chat_enabled"
private const val FEATURE_CHAT_SEND_MESSAGE = "feature_chat_send_message_enabled"
