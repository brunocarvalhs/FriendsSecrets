package br.com.brunocarvalhs.friendssecrets.common.extensions

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Base64
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class UriExtensionsTest {

    private lateinit var context: Context
    private lateinit var contentResolver: ContentResolver
    private lateinit var uri: Uri
    private lateinit var bitmap: Bitmap
    private lateinit var source: ImageDecoder.Source

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        contentResolver = mockk()
        uri = mockk()
        bitmap = mockk()
        source = mockk()

        every { context.contentResolver } returns contentResolver

        // Mocka Uri.toString para toDecodeBase64
        every { uri.toString() } returns Base64.encodeToString("test".toByteArray(), Base64.NO_WRAP)

        mockkStatic(ImageDecoder::class)
        every { ImageDecoder.createSource(contentResolver, uri) } returns source
        every { ImageDecoder.decodeBitmap(source) } returns bitmap

        mockkStatic(Bitmap::class)
        every {
            Bitmap.createScaledBitmap(bitmap, any(), any(), true)
        } returns bitmap

        mockkStatic(Base64::class)
        every { Base64.encodeToString(any<ByteArray>(), Base64.NO_WRAP) } returns "base64string"
        every { Base64.decode(any<String>(), Base64.NO_WRAP) } returns "decodedbytes".toByteArray()

        every { bitmap.width } returns 400
        every { bitmap.height } returns 300
        every { bitmap.compress(any(), any(), any()) } answers {
            // do nothing for compress, pretend success
            true
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `toBase64 returns base64 string on success`() {
        val base64 = uri.toBase64(context, maxSize = 200)

        assertNotNull(base64)
        assertEquals("base64string", base64)
        verify { ImageDecoder.createSource(contentResolver, uri) }
        verify { ImageDecoder.decodeBitmap(source) }
        verify { Bitmap.createScaledBitmap(bitmap, any(), any(), true) }
        verify { bitmap.compress(Bitmap.CompressFormat.JPEG, 80, any()) }
        verify { Base64.encodeToString(any(), Base64.NO_WRAP) }
    }

    @Test
    fun `toBase64 returns null on exception`() {
        // Forcing ImageDecoder to throw exception
        every { ImageDecoder.createSource(contentResolver, uri) } throws RuntimeException("fail")

        val base64 = uri.toBase64(context)

        assertNull(base64)
    }

    @Test
    fun `toDecodeBase64 returns decoded bytes`() {
        val bytes = uri.toDecodeBase64()
        assertNotNull(bytes)
        assertEquals("decodedbytes".toByteArray().toList(), bytes?.toList())
    }

    @Test
    fun `toDecodeBase64 returns null on decode exception`() {
        every { uri.toString() } returns "invalidbase64string!"

        val bytes = uri.toDecodeBase64()

        assertNull(bytes)
    }

    @Test
    fun `toBitmap returns bitmap on success`() {
        val result = uri.toBitmap(context)
        assertNotNull(result)
        verify { ImageDecoder.createSource(contentResolver, uri) }
        verify { ImageDecoder.decodeBitmap(source) }
    }

    @Test
    fun `toBitmap returns null on exception`() {
        every { ImageDecoder.createSource(contentResolver, uri) } throws RuntimeException("fail")

        val result = uri.toBitmap(context)

        assertNull(result)
    }
}
