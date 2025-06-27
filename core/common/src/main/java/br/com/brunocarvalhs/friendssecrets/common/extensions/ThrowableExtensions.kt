package br.com.brunocarvalhs.friendssecrets.common.extensions

import br.com.brunocarvalhs.friendssecrets.common.logger.crashlytics.CrashlyticsProvider
import timber.log.Timber

fun Throwable?.report(params: Map<String, String>? = null): Throwable? {
    if (this == null) return null
    Timber.e(this)
    CrashlyticsProvider.getInstance().report(throwable = this, params = params)
    return this
}