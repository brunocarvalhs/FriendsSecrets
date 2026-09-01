package br.com.brunocarvalhs.friendssecrets.initializers

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck

class AppCheckInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        Firebase.appCheck.installAppCheckProviderFactory(provideAppCheckProviderFactory())
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return mutableListOf(FirebaseInitializer::class.java)
    }
}
