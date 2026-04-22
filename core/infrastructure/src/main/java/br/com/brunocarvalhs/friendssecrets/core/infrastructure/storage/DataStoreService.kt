package br.com.brunocarvalhs.friendssecrets.core.infrastructure.storage

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

private val Context.dataStore by preferencesDataStore(name = "friends_secrets_prefs")

@Singleton
class DataStoreService @Inject constructor(
    @ApplicationContext private val context: Context
) : StorageService {

    override suspend fun <T : Any> save(key: String, value: T) {
        val prefKey = getPreferencesKey(key, value::class) as Preferences.Key<T>
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    override suspend fun <T : Any> load(key: String, clazz: KClass<T>): T? {
        val prefKey = getPreferencesKey(key, clazz)
        return context.dataStore.data.map { preferences ->
            preferences[prefKey]
        }.first() as? T
    }

    override suspend fun remove(key: String) {
        // Implement removal if needed in StorageService interface
    }

    private fun <T : Any> getPreferencesKey(key: String, clazz: KClass<T>): Preferences.Key<*> {
        return when (clazz) {
            String::class -> stringPreferencesKey(key)
            Int::class -> intPreferencesKey(key)
            Long::class -> longPreferencesKey(key)
            Float::class -> floatPreferencesKey(key)
            Double::class -> doublePreferencesKey(key)
            Boolean::class -> booleanPreferencesKey(key)
            else -> throw IllegalArgumentException("Unsupported type: ${clazz.simpleName}")
        }
    }
}
