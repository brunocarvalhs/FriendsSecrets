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
            setPersistenceCacheSizeBytes(CACHE_SIZE_BYTES)
            setLogLevel(Logger.Level.DEBUG)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return mutableListOf(FirebaseInitializer::class.java)
    }

    companion object {
        private const val CACHE_SIZE_MB = 50L
        private const val BYTES_PER_KB = 1024L
        private const val KB_PER_MB = 1024L
        private const val CACHE_SIZE_BYTES = CACHE_SIZE_MB * BYTES_PER_KB * KB_PER_MB
    }
}
