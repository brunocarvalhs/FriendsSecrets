package br.com.brunocarvalhs.friendssecrets.initializers

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger

class RealtimeInitializer: Initializer<FirebaseDatabase> {

    override fun create(context: Context): FirebaseDatabase {
        return FirebaseDatabase.getInstance().apply {
            setPersistenceEnabled(!BuildConfig.DEBUG)
            setPersistenceCacheSizeBytes(50 * 1024 * 1024)
            setLogLevel(Logger.Level.DEBUG)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return mutableListOf(FirebaseInitializer::class.java)
    }
}