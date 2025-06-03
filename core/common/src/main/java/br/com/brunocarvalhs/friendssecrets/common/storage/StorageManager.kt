package br.com.brunocarvalhs.friendssecrets.common.storage

import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import javax.inject.Inject

class StorageManager @Inject constructor(
    private val storage: StorageEvent
) : StorageService {

    override fun <T> save(key: String, value: T) = storage.save(key, value)

    override fun <T> load(key: String, clazz: Class<T>): T? = storage.load(key, clazz)

    override fun remove(key: String) = storage.remove(key)

    interface StorageEvent {
        fun <T> save(key: String, value: T)
        fun <T> load(key: String, clazz: Class<T>): T?
        fun remove(key: String)
    }
}

