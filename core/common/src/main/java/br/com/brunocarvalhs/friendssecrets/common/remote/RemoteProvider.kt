package br.com.brunocarvalhs.friendssecrets.common.remote

import timber.log.Timber

class RemoteProvider(
    private val event: RemoteEvent
) {

    fun default(defaultValues: Map<String, Any>) {
        event.defaultValues(defaultValues)
    }

    fun getLong(key: String): Long {
        return event.getValue(key, 0L)
    }

    fun getBoolean(key: String): Boolean {
        return event.getValue(key, false)
    }

    fun getString(key: String): String {
        return event.getValue(key, "")
    }

    suspend fun getAsyncString(key: String): String {
        return event.getValueAsync(key, "")
    }

    fun getDouble(key: String): Double {
        return event.getValue(key, 0.0)
    }

    fun fetchAndActivate() {
        return event.fetchAndActivate()
    }

    interface RemoteEvent {
        fun <T> getValue(key: String, defaultValue: T): T
        suspend fun <T> getValueAsync(key: String, defaultValue: T): T
        fun defaultValues(defaultValues: Map<String, Any>)
        fun fetchAndActivate()
    }

    companion object {
        @Volatile
        private var instance: RemoteProvider? = null

        fun initialize(event: RemoteEvent) {
            synchronized(this) {
                if (instance == null) {
                    instance = RemoteProvider(event)
                } else {
                    Timber.tag("RemoteProvider").w("Provider já inicializado.")
                }
            }
        }

        fun getInstance(): RemoteProvider {
            return instance ?: synchronized(this) {
                instance
                    ?: throw IllegalStateException("RemoteProvider não inicializado. Chame initialize() primeiro.")
            }
        }
    }
}
