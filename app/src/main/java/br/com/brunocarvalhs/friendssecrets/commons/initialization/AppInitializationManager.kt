package br.com.brunocarvalhs.friendssecrets.commons.initialization

/**
 * Gerenciador de inicialização do aplicativo.
 * Responsável por inicializar todos os componentes necessários na inicialização do app.
 */
class AppInitializationManager(
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
