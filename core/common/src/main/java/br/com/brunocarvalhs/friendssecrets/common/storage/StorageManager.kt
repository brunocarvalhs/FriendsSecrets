package br.com.brunocarvalhs.friendssecrets.common.storage

import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import timber.log.Timber

class StorageManager(
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

    companion object {
        @Volatile
        private var instance: StorageManager? = null

        fun initialize(event: StorageEvent) {
            synchronized(this) {
                if (instance == null) {
                    instance = StorageManager(event)
                } else {
                    Timber.tag("StorageManager").w("StorageManager já inicializado.")
                }
            }
        }

        fun getInstance(): StorageManager {
            return instance ?: synchronized(this) {
                instance ?: throw IllegalStateException(
                    "StorageManager não inicializado. Chame initialize() primeiro."
                )
            }
        }
    }
}

