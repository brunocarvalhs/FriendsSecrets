package br.com.brunocarvalhs.friendssecrets.initialization.providers

import android.content.SharedPreferences
import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager
import com.google.gson.Gson
import timber.log.Timber

class StorageEventImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson = Gson()
) : StorageManager.StorageEvent {

    override fun <T> save(key: String, value: T) {
        with(sharedPreferences.edit()) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Long -> putLong(key, value)
                else -> putString(key, gson.toJson(value))
            }
            apply()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> load(key: String, clazz: Class<T>): T? {
        return try {
            when (clazz) {
                String::class.java -> sharedPreferences.getString(key, null) as? T
                Int::class.java -> sharedPreferences.getInt(key, 0) as? T
                Boolean::class.java -> sharedPreferences.getBoolean(key, false) as? T
                Long::class.java -> sharedPreferences.getLong(key, 0L) as? T
                else -> {
                    val json = sharedPreferences.getString(key, null)
                    gson.fromJson(json, clazz)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Erro ao carregar chave $key para tipo ${clazz.simpleName}")
            null
        }
    }

    override fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}
