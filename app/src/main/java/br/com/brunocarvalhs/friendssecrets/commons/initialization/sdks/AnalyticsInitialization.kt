package br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.commons.initialization.AppInitialization

class AnalyticsInitialization(private val context: Context) : AppInitialization() {

    override fun tag(): String = "AnalyticsInitialization"

    override fun execute() {
        AnalyticsProvider.setUserId(context)
    }
}