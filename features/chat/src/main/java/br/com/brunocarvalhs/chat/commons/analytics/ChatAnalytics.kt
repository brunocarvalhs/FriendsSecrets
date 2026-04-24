package br.com.brunocarvalhs.chat.commons.analytics

interface ChatAnalytics {
    fun trackScreenView()
    fun trackSendMessage()
    fun trackClearMessages()
}

