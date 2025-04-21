package br.com.brunocarvalhs.friendssecrets.commons.initialization

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitializationManager @Inject constructor(
    private val initializations: List<AppInitialization>
) {
    fun initialize() {
        initializations.forEach { initialization ->
            initialization.start()
            initialization.execute()
            initialization.stop()
        }
    }
}