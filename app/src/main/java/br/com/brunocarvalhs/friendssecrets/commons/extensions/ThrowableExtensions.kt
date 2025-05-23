package br.com.brunocarvalhs.friendssecrets.commons.extensions

import br.com.brunocarvalhs.friendssecrets.commons.logger.crashlytics.CrashlyticsProvider
import com.google.firebase.perf.metrics.AddTrace

/**
 * Extension function to report a Throwable to Crashlytics.
 *
 * @param params Optional parameters to include with the report.
 * @return The Throwable instance for chaining.
 */
@JvmName("reportThrowable")
@AddTrace(name = "Throwable.report")
fun Throwable?.report(params: Map<String, String>? = null): Throwable? {
    if (this == null) return null
    CrashlyticsProvider.report(throwable = this, params = params)
    return this
}