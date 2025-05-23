package br.com.brunocarvalhs.friendssecrets.commons.extensions

import br.com.brunocarvalhs.friendssecrets.commons.logger.crashlytics.CrashlyticsProvider

fun Throwable?.report(params: Map<String, String>? = null): Throwable? {
    if (this == null) return null
    CrashlyticsProvider.report(throwable = this, params = params)
    return this
}