package br.com.brunocarvalhs.friendssecrets.common.storage

import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import javax.inject.Inject

class StorageManager @Inject constructor(
    private val storage: StorageEvent
) : StorageService {

    override suspend fun <T> save(key: String, value: T) = storage.save(key, value)

    override suspend fun <T> load(key: String, clazz: Class<T>): T? = storage.load(key, clazz)

    override suspend fun remove(key: String) = storage.remove(key)

    interface StorageEvent {
        suspend fun <T> save(key: String, value: T)
        suspend fun <T> load(key: String, clazz: Class<T>): T?
        suspend fun remove(key: String)
    }
}

