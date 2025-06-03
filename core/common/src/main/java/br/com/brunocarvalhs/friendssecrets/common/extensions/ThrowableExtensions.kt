package br.com.brunocarvalhs.friendssecrets.common.extensions

import timber.log.Timber

fun Throwable?.report(params: Map<String, String>? = null): Throwable? {
    if (this == null) return null
    Timber.e(this)
    return this
}