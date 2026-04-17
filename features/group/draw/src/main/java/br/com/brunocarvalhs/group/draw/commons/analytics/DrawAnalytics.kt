package br.com.brunocarvalhs.group.draw.commons.analytics

internal interface DrawAnalytics {
    fun trackDrawAction()
    fun trackShareAction()
    fun trackScreenView()
}

