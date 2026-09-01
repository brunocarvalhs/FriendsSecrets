package br.com.brunocarvalhs.chat.app.data.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val PATTERN_DATE = "HH:mm"

internal fun Long.toLocalDateTime(): String {
    return SimpleDateFormat(PATTERN_DATE, Locale.getDefault())
        .format(java.util.Date(this))
}

internal fun Long.isSameDayAs(other: Long): Boolean {
    val calendar = Calendar.getInstance().apply { timeInMillis = this@isSameDayAs }
    val otherCalendar = Calendar.getInstance().apply { timeInMillis = other }
    return calendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR) &&
        calendar.get(Calendar.DAY_OF_YEAR) == otherCalendar.get(Calendar.DAY_OF_YEAR)
}
