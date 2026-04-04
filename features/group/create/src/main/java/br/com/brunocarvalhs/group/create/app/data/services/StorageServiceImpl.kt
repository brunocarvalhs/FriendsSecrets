package br.com.brunocarvalhs.group.create.app.data.services

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import br.com.brunocarvalhs.group.create.app.domain.services.StorageService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageServiceImpl @Inject constructor(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    },
    private val dataStore: DataStore<Preferences>
) : StorageService {

    override suspend fun <T> save(key: String, value: T) {
        val dataStoreKey = stringPreferencesKey(key)
        val jsonValue = run {
            @Suppress("UNCHECKED_CAST")
            val serializer =
                json.serializersModule.serializer(value!!::class.java) as KSerializer<T>
            json.encodeToString(serializer, value)
        }

        jsonValue.let {
            dataStore.edit { preferences ->
                preferences[dataStoreKey] = it
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> load(key: String, value: KClass<T>): T? {
        val dataStoreKey = stringPreferencesKey(key)
        val jsonValue = dataStore.data
            .map { preferences -> preferences[dataStoreKey] }
            .firstOrNull() ?: return null

        return run {
            val serializer = json.serializersModule.serializer(value.java) as KSerializer<T>
            json.decodeFromString(serializer, jsonValue)
        }
    }
}
