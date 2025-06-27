package br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics

class CrashlyticsProvider(
    private val event: CrashlyticsEvent
) {
    init {
        instance = this
    }

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
        fun setUserId(id: String)
    }

    companion object {
        @Volatile
        private var instance: CrashlyticsProvider? = null

        @JvmStatic
        fun getInstance(): CrashlyticsProvider {
            return instance ?: throw IllegalStateException("CrashlyticsProvider not initialized")
        }

        @JvmStatic
        fun initialize(event: CrashlyticsEvent) {
            if (instance == null) {
                instance = CrashlyticsProvider(event)
            }
        }
    }
}
