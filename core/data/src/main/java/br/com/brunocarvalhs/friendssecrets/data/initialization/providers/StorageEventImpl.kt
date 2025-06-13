package br.com.brunocarvalhs.friendssecrets.data.initialization.providers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import timber.log.Timber

class StorageEventImpl(
    private val context: Context,
) : StorageManager.StorageEvent {

    private val gson = Gson()

    private val Context.dataStore by preferencesDataStore(name = "${context.packageName}_storage")

    override suspend fun <T> save(key: String, value: T) {
        try {
            val preferencesKey = stringPreferencesKey(key)
            val stringValue = when (value) {
                is String -> value
                is Int, is Boolean, is Long -> value.toString()
                else -> gson.toJson(value)
            }

            context.dataStore.edit { preferences ->
                preferences[preferencesKey] = stringValue
            }
        } catch (e: Exception) {
            Timber.e(e, "Erro ao salvar chave $key com valor $value")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> load(key: String, clazz: Class<T>): T? {
        return try {
            val preferences = context.dataStore.data.first()
            val storedValue = preferences[stringPreferencesKey(key)] ?: return null

            when (clazz) {
                String::class.java -> storedValue as? T
                Int::class.java -> storedValue.toIntOrNull() as? T
                Boolean::class.java -> storedValue.toBooleanStrictOrNull() as? T
                Long::class.java -> storedValue.toLongOrNull() as? T
                else -> gson.fromJson(storedValue, clazz)
            }
        } catch (e: Exception) {
            Timber.e(e, "Erro ao carregar chave $key para tipo ${clazz.simpleName}")
            null
        }
    }

    override suspend fun remove(key: String) {
        try {
            context.dataStore.edit { preferences ->
                preferences.remove(stringPreferencesKey(key))
            }
        } catch (e: Exception) {
            Timber.e(e, "Erro ao remover chave $key")
        }
    }
}