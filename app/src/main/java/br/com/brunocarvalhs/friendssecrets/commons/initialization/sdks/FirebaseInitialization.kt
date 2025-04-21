package br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.commons.initialization.AppInitialization
import com.google.firebase.FirebaseApp

class FirebaseInitialization(private val context: Context) : AppInitialization() {

    override fun execute() {
        FirebaseApp.initializeApp(context)
    }

    override fun tag(): String = "FirebaseInitialization"
}