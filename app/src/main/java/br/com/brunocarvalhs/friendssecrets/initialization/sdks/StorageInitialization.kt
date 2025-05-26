package br.com.brunocarvalhs.friendssecrets.initialization.sdks

import android.content.Context
import android.content.SharedPreferences
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager
import br.com.brunocarvalhs.friendssecrets.initialization.providers.StorageEventImpl

class StorageInitialization : Initializer<StorageManager> {
    override fun create(context: Context): StorageManager {
        val storageName: String = BuildConfig.APPLICATION_ID
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(storageName, Context.MODE_PRIVATE)
        return StorageManager(
            storage = StorageEventImpl(sharedPreferences)
        )
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(TimberInitialization::class.java)
    }
}