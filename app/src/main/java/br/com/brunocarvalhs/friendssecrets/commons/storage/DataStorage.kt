package br.com.brunocarvalhs.friendssecrets.commons.storage

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val DATASTORE_NAME = "friends_secrets_prefs"
private val CORRUPTION_HANDLER: ReplaceFileCorruptionHandler<Preferences>? = null
private val PRODUCE_MIGRATIONS: (Context) -> List<DataMigration<Preferences>> = { listOf() }

internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATASTORE_NAME,
    corruptionHandler = CORRUPTION_HANDLER,
    produceMigrations = PRODUCE_MIGRATIONS
)