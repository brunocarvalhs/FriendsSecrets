package br.com.brunocarvalhs.friendssecrets.data.service

import br.com.brunocarvalhs.friendssecrets.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class GenerativeServiceTest {

    private lateinit var generativeService: GenerativeService
    private lateinit var mockGenerativeModel: GenerativeModel

    @Before
    fun setup() {
        mockGenerativeModel = mockk(relaxed = true)

        generativeService = GenerativeService(
            modelName = BuildConfig.MODEL_NAME,
            apiKey = BuildConfig.apiKey,
            generativeModel = mockGenerativeModel
        )
    }

    @Test
    fun `test invoke method with prompt`() = runBlocking {
        val prompt = "Generate a landscape"
        val expectedResponseText = "Generated landscape image text"

        coEvery {
            mockGenerativeModel.generateContent(any<String>())
        } returns mockk {
            every { text } returns expectedResponseText
        }

        val result = generativeService.invoke(prompt)

        coVerify {
            mockGenerativeModel.generateContent(prompt)
        }
        assertEquals(expectedResponseText, result)
    }
}