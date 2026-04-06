package br.com.brunocarvalhs.group.list.app.data.services

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import br.com.brunocarvalhs.group.details.app.domain.services.StorageService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageServiceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }
) : StorageService {

    override suspend fun <T> save(key: String, value: T) {
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
                        val serializer = json.serializersModule.serializer(value!!::class.java) as KSerializer<T>
                        preferences[stringPreferencesKey(key)] = json.encodeToString(serializer, value)
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> load(key: String, value: KClass<T>): T? {
        val preferences = dataStore.data.firstOrNull() ?: return null
        
        return runCatching {
            when {
                value == Set::class || value == Array::class -> {
                    val stringSet = preferences[stringSetPreferencesKey(key)]
                    if (value == Array::class) stringSet?.toTypedArray() as? T else stringSet as? T
                }
                value == String::class -> preferences[stringPreferencesKey(key)] as? T
                else -> {
                    val jsonValue = preferences[stringPreferencesKey(key)] ?: return null
                    val serializer = json.serializersModule.serializer(value.java) as KSerializer<T>
                    json.decodeFromString(serializer, jsonValue)
                }
            }
        }.getOrNull()
    }
}
