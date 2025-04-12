package br.com.brunocarvalhs.friendssecrets.commons.initialization

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.CrashlyticsInitialization
import br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks.TimberInitialization

class AppInitializationManager(private val context: Context) {

    private val initializations: List<AppInitialization> = listOf(
        TimberInitialization(),
        CrashlyticsInitialization(context),
    )

    fun initialize() {
        initializations.forEach { initialization ->
            initialization.start()
            initialization.execute()
            initialization.stop()
        }
    }
}