package br.com.brunocarvalhs.chat.commons.analytics

internal interface ChatAnalytics {
    fun trackScreenView()
    fun trackSendMessage()
    fun trackClearMessages()
}

