package br.com.brunocarvalhs.friendssecrets.common.extensions

import br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics.CrashlyticsProvider
import timber.log.Timber

fun Throwable?.report(params: Map<String, String>? = null): Throwable? {
    if (this == null) return null
    CrashlyticsProvider.getInstance().report(this, params)
    return this
}