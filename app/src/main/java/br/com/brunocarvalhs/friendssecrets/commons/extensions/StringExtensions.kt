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