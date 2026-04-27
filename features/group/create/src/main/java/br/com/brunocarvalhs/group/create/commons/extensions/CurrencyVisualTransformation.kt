package br.com.brunocarvalhs.group.create.commons.extensions

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

internal class CurrencyVisualTransformation : VisualTransformation {

    companion object {
        private const val CENTS_DIGITS = 2
        private const val MIN_PAD_LENGTH = 3
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val clean = text.text.filter { it.isDigit() }

        if (clean.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val padded = clean.padStart(MIN_PAD_LENGTH, '0')
        val cents = padded.takeLast(CENTS_DIGITS)
        val reais = padded.dropLast(CENTS_DIGITS).toLongOrNull()?.toString() ?: "0"

        val formatted = "R$ $reais,$cents"

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = formatted.length
            override fun transformedToOriginal(offset: Int): Int = text.length
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}
