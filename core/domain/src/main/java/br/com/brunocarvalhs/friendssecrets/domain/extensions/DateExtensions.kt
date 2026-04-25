package br.com.brunocarvalhs.friendssecrets.domain.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDate(pattern: String = "dd/MM/yyyy"): String {
    if (this == 0L) return ""
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.format(Date(this))
    } catch (_: IllegalArgumentException) {
        ""
    }
}

fun Long.toFormattedDateTime(pattern: String = "dd/MM/yyyy HH:mm"): String {
    return this.toFormattedDate(pattern)
}
