package br.com.brunocarvalhs.friendssecrets.data.service

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GenerativeServiceImplTest {
    private lateinit var generativeModel: Lazy<GenerativeModel>
    private lateinit var service: GenerativeServiceImpl

    @Before
    fun setUp() {
        generativeModel = mockk()
        service = GenerativeServiceImpl(generativeModel)
    }

    @Test
    fun `invoke should return response text`() = runTest {
        val prompt = "test prompt"
        val response = mockk<GenerateContentResponse>()
        coEvery { generativeModel.get().generateContent(prompt) } returns response
        coEvery { response.text } returns "result text"
        val result = service.invoke(prompt)
        assertEquals("result text", result)
    }

    @Test
    fun `invoke should return null if response text is null`() = runTest {
        val prompt = "test prompt"
        val response = mockk<GenerateContentResponse>()
        coEvery { generativeModel.get().generateContent(prompt) } returns response
        coEvery { response.text } returns null
        val result = service.invoke(prompt)
        assertEquals(null, result)
    }
}

