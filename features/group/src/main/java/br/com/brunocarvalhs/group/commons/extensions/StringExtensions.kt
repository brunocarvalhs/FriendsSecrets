package br.com.brunocarvalhs.auth.commons.extensions

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

internal fun String.toDateMask(): VisualTransformation {
    val trimmed = this.take(8)
    val masked = buildString {
        for (i in trimmed.indices) {
            append(trimmed[i])
            if (i == 1 || i == 3) append('/')
        }
    }
    return VisualTransformation {
        TransformedText(
            AnnotatedString(masked),
            OffsetMapping.Identity
        )
    }
}

fun String.toCurrencyMask(): VisualTransformation {
    val clean = filter { it.isDigit() }
    val padded = clean.padStart(3, '0')
    val cents = padded.takeLast(2)
    val reais = padded.dropLast(2).toBigInteger().toString()
    val formatted = "R$ ${reais.toIntOrNull() ?: 0},$cents"
    return VisualTransformation {
        TransformedText(
            AnnotatedString(formatted),
            OffsetMapping.Identity
        )
    }
}
