package br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks

import br.com.brunocarvalhs.friendssecrets.commons.initialization.AppInitialization
import br.com.brunocarvalhs.friendssecrets.commons.remote.RemoteProvider

class RemoteInitialization : AppInitialization() {

    override fun tag(): String = "RemoteInitialization"

    override fun execute() {
        RemoteProvider().fetchAndActivate()
    }
}