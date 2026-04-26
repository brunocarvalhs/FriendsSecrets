package br.com.brunocarvalhs.chat.commons.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

class ChatAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : ChatAnalytics {
    override fun trackScreenView() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "ChatScreen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ChatViewModel")
        }
    }

    override fun trackSendMessage() {
        firebaseAnalytics.logEvent("chat_send_message", null)
    }

    override fun trackClearMessages() {
        firebaseAnalytics.logEvent("chat_clear_messages", null)
    }
}
