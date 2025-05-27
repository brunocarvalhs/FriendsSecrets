package br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics

import timber.log.Timber

class CrashlyticsProvider(
    private val event: CrashlyticsEvent
) {

    fun report(throwable: Throwable, params: Map<String, String>? = null) {
        params?.forEach { (key, value) -> setCustomKey(key, value) }
        event.report(throwable)
    }

    fun log(message: String, params: Map<String, String>? = null) {
        params?.forEach { (key, value) -> setCustomKey(key, value) }
        event.log(message)
    }

    private fun setCustomKey(key: String, value: String) {
        event.parameter(key, value)
    }

    interface CrashlyticsEvent {
        fun report(throwable: Throwable)
        fun log(message: String)
        fun parameter(key: String, value: String)
    }

    companion object {
        @Volatile
        private var instance: CrashlyticsProvider? = null

        fun initialize(event: CrashlyticsEvent) {
            synchronized(this) {
                if (instance == null) {
                    instance = CrashlyticsProvider(event)
                } else {
                    Timber.tag("CrashlyticsProvider").w("Provider já inicializado.")
                }
            }
        }

        fun getInstance(): CrashlyticsProvider {
            return instance ?: synchronized(this) {
                instance
                    ?: throw IllegalStateException("CrashlyticsProvider não inicializado. Chame initialize() primeiro.")
            }
        }
    }
}
