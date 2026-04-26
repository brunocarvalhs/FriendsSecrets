package br.com.brunocarvalhs.group.create.commons.extensions

import androidx.compose.ui.text.AnnotatedString
import io.mockk.MockKAnnotations
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrencyVisualTransformationTest {

    private lateinit var transformation: CurrencyVisualTransformation

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        transformation = CurrencyVisualTransformation()
    }

    @Test
    fun `deve formatar valor vazio como string vazia`() {
        val result = transformation.filter(AnnotatedString(""))

        assertEquals("", result.text.text)
    }

    @Test
    fun `deve formatar apenas números corretamente`() {
        val input = AnnotatedString("12345") // 123,45

        val result = transformation.filter(input)

        assertEquals("R$ 123,45", result.text.text)
    }

    @Test
    fun `deve preencher com zeros à esquerda quando necessário`() {
        val input = AnnotatedString("5") // 0,05

        val result = transformation.filter(input)

        assertEquals("R$ 0,05", result.text.text)
    }

    @Test
    fun `deve remover caracteres não numéricos`() {
        val input = AnnotatedString("R$ 1a2b3c4") // 12,34

        val result = transformation.filter(input)

        assertEquals("R$ 12,34", result.text.text)
    }

    @Test
    fun `deve manter estrutura de moeda BRL`() {
        val input = AnnotatedString("999999")

        val result = transformation.filter(input)

        assertEquals("R$ 9999,99", result.text.text)
    }
}
