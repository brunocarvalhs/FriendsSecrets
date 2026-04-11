package br.com.brunocarvalhs.friendssecrets.domain.extensions

import java.text.NumberFormat
import java.util.Locale

fun Double?.toCurrencyMask(): String {
    if (this == null) return ""
    return try {
        val valueToFormat = this / 10
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        format.format(valueToFormat)
    } catch (e: Exception) {
        this.toString()
    }
}
