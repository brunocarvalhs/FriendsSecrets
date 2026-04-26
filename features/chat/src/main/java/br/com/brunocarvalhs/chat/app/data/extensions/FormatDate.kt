package br.com.brunocarvalhs.chat.app.data.extensions

import java.text.SimpleDateFormat
import java.util.Locale

private const val PATTERN_DATE = "HH:mm"

internal fun Long.toLocalDateTime(): String {
    return SimpleDateFormat(PATTERN_DATE, Locale.getDefault())
        .format(java.util.Date(this))
}
