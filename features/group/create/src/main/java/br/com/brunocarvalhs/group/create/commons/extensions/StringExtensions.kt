package br.com.brunocarvalhs.group.create.commons.extensions

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val clean = text.text.filter { it.isDigit() }
        if (clean.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val padded = clean.padStart(3, '0')
        val cents = padded.takeLast(2)
        val reais = padded.dropLast(2).toLongOrNull()?.toString() ?: "0"
        val formatted = "R$ $reais,$cents"

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return formatted.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return text.length
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

fun String.toCurrencyMask(): VisualTransformation = CurrencyVisualTransformation()
