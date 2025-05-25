package br.com.brunocarvalhs.friendssecrets.common.storage

import timber.log.Timber

class StorageManager(
    private val storage: StorageEvent
) {

    fun <T> save(key: String, value: T) {
        storage.save(key, value)
    }

    fun <T> load(key: String): T? {
        return storage.load(key)
    }

    fun remove(key: String) {
        storage.remove(key)
    }

    interface StorageEvent {
        fun <T> save(key: String, value: T)
        fun <T> load(key: String): T?
        fun remove(key: String)
    }

    companion object {
        private var instance: StorageManager? = null

        fun initialize(event: StorageEvent) {
            synchronized(this) {
                if (instance == null) {
                    instance = StorageManager(event)
                } else {
                    Timber.tag("StorageManager").w("Provider já inicializado.")
                }
            }
        }

        fun getInstance(): StorageManager {
            return instance ?: synchronized(this) {
                instance
                    ?: throw IllegalStateException("StorageManager não inicializado. Chame initialize() primeiro.")
            }
        }
    }
}