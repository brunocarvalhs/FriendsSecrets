package br.com.brunocarvalhs.group.create.app.data.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.brunocarvalhs.group.create.app.domain.services.StorageService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class StorageServiceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val json: Json
) : StorageService {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = context.packageName)

    override suspend fun <T : Any> save(key: String, value: KClass<T>) {
        val dataStoreKey = stringPreferencesKey(key)
        val jsonValue = runCatching {
            val serializer = json.serializersModule.serializer(value::class.java)
            json.encodeToString(serializer, value)
        }.getOrNull()

        jsonValue?.let {
            context.dataStore.edit { preferences ->
                preferences[dataStoreKey] = it
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> load(key: String, value: KClass<T>): T? {
        val dataStoreKey = stringPreferencesKey(key)
        val jsonValue = context.dataStore.data
            .map { preferences -> preferences[dataStoreKey] }
            .firstOrNull() ?: return null

        return runCatching {
            val serializer = json.serializersModule.serializer(value::class.java)
            json.decodeFromString(serializer, jsonValue) as? T
        }.getOrNull()
    }
}
