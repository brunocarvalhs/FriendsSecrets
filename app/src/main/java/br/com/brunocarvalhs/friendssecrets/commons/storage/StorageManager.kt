package br.com.brunocarvalhs.friendssecrets.commons.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import timber.log.Timber
import javax.inject.Inject
import kotlin.reflect.KClass

class StorageManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }
) : StorageService {

    private val TAG = "StorageManager"

    override suspend fun <T : Any> save(key: String, value: T) {
        Timber.tag(TAG).d("--> SAVE [%s]", key)
        Timber.tag(TAG).v("Value: %s", value)
        
        runCatching {
            dataStore.edit { preferences ->
                when (value) {
                    is Set<*> -> {
                        val stringSet = value.filterIsInstance<String>().toSet()
                        preferences[stringSetPreferencesKey(key)] = stringSet
                    }

                    is Array<*> -> {
                        val stringSet = value.filterIsInstance<String>().toSet()
                        preferences[stringSetPreferencesKey(key)] = stringSet
                    }

                    is String -> preferences[stringPreferencesKey(key)] = value
                    else -> {
                        @Suppress("UNCHECKED_CAST")
                        val serializer =
                            json.serializersModule.serializer(value!!::class.java) as KSerializer<T>
                        preferences[stringPreferencesKey(key)] =
                            json.encodeToString(serializer, value)
                    }
                }
            }
        }.onSuccess {
            Timber.tag(TAG).d("<-- SUCCESS SAVE [%s]", key)
        }.onFailure {
            Timber.tag(TAG).e(it, "<-- FAILURE SAVE [%s] | Error: %s", key, it.message)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> load(key: String, value: KClass<T>): T? {
        Timber.tag(TAG).d("--> LOAD [%s] | Type: %s", key, value.simpleName)
        
        val preferences = dataStore.data.firstOrNull() ?: run {
            Timber.tag(TAG).w("<-- LOAD [%s] | DataStore is empty", key)
            return null
        }

        return runCatching {
            when {
                value == Set::class || value.java.isAssignableFrom(Set::class.java) -> {
                    preferences[stringSetPreferencesKey(key)] as? T
                }

                value == Array::class || value.java.isArray -> {
                    preferences[stringSetPreferencesKey(key)]?.toTypedArray() as? T
                }

                value == String::class -> {
                    preferences[stringPreferencesKey(key)] as? T
                }

                else -> {
                    val jsonValue = preferences[stringPreferencesKey(key)] ?: run {
                        Timber.tag(TAG).d("<-- LOAD [%s] | Not found", key)
                        return null
                    }
                    val serializer = json.serializersModule.serializer(value.java) as KSerializer<T>
                    json.decodeFromString(serializer, jsonValue)
                }
            }
        }.onSuccess { result ->
            if (result != null) {
                Timber.tag(TAG).d("<-- SUCCESS LOAD [%s]", key)
                Timber.tag(TAG).v("Result: %s", result)
            } else {
                Timber.tag(TAG).d("<-- LOAD [%s] | Value is null", key)
            }
        }.onFailure {
            Timber.tag(TAG).e(it, "<-- FAILURE LOAD [%s] | Error: %s", key, it.message)
        }.getOrNull()
    }

    override suspend fun remove(key: String) {
        Timber.tag(TAG).d("--> REMOVE [%s]", key)
        runCatching {
            dataStore.edit { preferences ->
                preferences.remove(stringPreferencesKey(key))
                preferences.remove(stringSetPreferencesKey(key))
            }
        }.onSuccess {
            Timber.tag(TAG).d("<-- SUCCESS REMOVE [%s]", key)
        }.onFailure {
            Timber.tag(TAG).e(it, "<-- FAILURE REMOVE [%s] | Error: %s", key, it.message)
        }
    }
}
