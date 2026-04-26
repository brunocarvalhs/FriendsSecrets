package br.com.brunocarvalhs.core.domain.extensions

import java.text.NumberFormat
import java.util.Locale

private const val CURRENCY_DIVISOR = 10

fun Double?.toCurrencyMask(
    locale: Locale = Locale.getDefault(),
    divisor: Int = CURRENCY_DIVISOR
): String {
    if (this == null) return ""
    val valueToFormat = this / divisor
    val formatter = NumberFormat.getCurrencyInstance(locale)
    return formatter.format(valueToFormat)
}
