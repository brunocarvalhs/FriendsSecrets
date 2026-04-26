package br.com.brunocarvalhs.friendssecrets.initializers

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp

class FirebaseInitializer: Initializer<FirebaseApp> {

    override fun create(context: Context): FirebaseApp {
        return FirebaseApp.getInstance()
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return mutableListOf()
    }
}

