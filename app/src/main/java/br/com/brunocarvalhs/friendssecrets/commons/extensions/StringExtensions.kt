package br.com.brunocarvalhs.friendssecrets.commons.extensions

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

fun String?.textWithFormatting(): String? {
    if (this == null) return null
    return buildAnnotatedString {
        val lines = this@textWithFormatting.split("\\n")
        for ((index, line) in lines.withIndex()) {
            if (index > 0) {
                append("\n")
            }
            val parts = line.split("**")
            for ((partIndex, part) in parts.withIndex()) {
                if (partIndex % 2 == 1) {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append(part)
                    pop()
                } else {
                    append(part)
                }
            }
        }
    }.text
}

fun String.toMaskedPhoneNumber(): String {
    // Remove tudo que não for número
    val cleaned = this.filter { it.isDigit() }

    return if (cleaned.length > 8) {
        val prefix = cleaned.take(6) // +DDDDDD (ex: +5511)
        val masked = "******"
        val suffix = cleaned.takeLast(2)
        "+$prefix$masked$suffix"
    } else {
        "+$cleaned"
    }
}
