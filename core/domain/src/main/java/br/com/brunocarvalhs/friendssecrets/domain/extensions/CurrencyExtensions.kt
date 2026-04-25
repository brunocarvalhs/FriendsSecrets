package br.com.brunocarvalhs.friendssecrets.domain.extensions

import java.text.NumberFormat
import java.util.Locale

private const val CURRENCY_DIVISOR = 10

fun Double?.toCurrencyMask(): String {
    if (this == null) return ""
    return try {
        val valueToFormat = this / CURRENCY_DIVISOR
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        format.format(valueToFormat)
    } catch (e: IllegalArgumentException) {
        this.toString()
    }
}
