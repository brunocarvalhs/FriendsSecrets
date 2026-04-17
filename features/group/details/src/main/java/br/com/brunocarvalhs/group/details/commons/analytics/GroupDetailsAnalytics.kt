package br.com.brunocarvalhs.group.details.commons.analytics

internal interface GroupDetailsAnalytics {
    fun trackScreenView()
    fun trackRefreshGroup()
    fun trackDeleteGroup()
    fun trackExitGroup()
    fun trackShareGroup()
}

