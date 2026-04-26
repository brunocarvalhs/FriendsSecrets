package br.com.brunocarvalhs.core.domain.extensions

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Locale

class ToCurrencyMaskTest {

    private lateinit var defaultLocale: Locale

    @Before
    fun setup() {
        defaultLocale = Locale.getDefault()
        Locale.setDefault(Locale("pt", "BR"))
    }

    @After
    fun tearDown() {
        Locale.setDefault(defaultLocale)
    }

    @Test
    fun `should return empty string when value is null`() {
        val result = (null as Double?).toCurrencyMask()

        assertEquals("", result)
    }

    @Test
    fun `should format value correctly in pt-BR`() {
        val result = 100.0.toCurrencyMask()

        assertEquals("R\$ 10,00", result)
    }

    @Test
    fun `should format value correctly in US locale`() {
        val result = 100.0.toCurrencyMask(locale = Locale.US)

        assertEquals("$10.00", result)
    }

    @Test
    fun `should divide value by divisor`() {
        val result = 200.0.toCurrencyMask(divisor = 10)

        assertTrue(result.contains("20"))
    }

    @Test
    fun `should work with different divisor`() {
        val result = 200.0.toCurrencyMask(divisor = 100)

        assertTrue(result.contains("2"))
    }

    @Test
    fun `should not crash on invalid input`() {
        val result = Double.MAX_VALUE.toCurrencyMask()

        assertTrue(result.isNotEmpty())
    }
}
