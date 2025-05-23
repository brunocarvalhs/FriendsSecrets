package br.com.brunocarvalhs.friendssecrets.commons.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Base64
import com.google.firebase.perf.metrics.AddTrace
import java.io.ByteArrayOutputStream

/**
 * Extension function to convert a Uri to a Base64 encoded string.
 *
 * @param context The context used to access the content resolver.
 * @param maxSize The maximum size for the resized bitmap (default is 200).
 * @return The Base64 encoded string or null if an error occurs.
 */
@JvmName("toBase64")
@AddTrace(name = "Uri.toBase64")
fun Uri.toBase64(context: Context, maxSize: Int = 200): String? {
    return try {
        val source = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.createSource(context.contentResolver, this)
        } else {
            return null
        }

        val originalBitmap = ImageDecoder.decodeBitmap(source)

        val ratio = minOf(
            maxSize.toFloat() / originalBitmap.width,
            maxSize.toFloat() / originalBitmap.height
        )
        val width = (originalBitmap.width * ratio).toInt()
        val height = (originalBitmap.height * ratio).toInt()
        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true)

        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val byteArray = outputStream.toByteArray()

        Base64.encodeToString(byteArray, Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Extension function to decode a Base64 encoded string to a ByteArray.
 *
 * @return The decoded ByteArray or null if an error occurs.
 */
@JvmName("toDecodeBase64")
@AddTrace(name = "Uri.toDecodeBase64")
fun Uri.toDecodeBase64(): ByteArray? {
    return try {
        val decodedBytes = Base64.decode(this.toString(), Base64.NO_WRAP)
        decodedBytes
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Extension function to convert a Uri to a Bitmap.
 *
 * @param context The context used to access the content resolver.
 * @return The Bitmap or null if an error occurs.
 */
@JvmName("toBitmap")
@AddTrace(name = "Uri.toBitmap")
fun Uri.toBitmap(context: Context): Bitmap? {
    return try {
        val source = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.createSource(context.contentResolver, this)
        } else {
            return null
        }
        ImageDecoder.decodeBitmap(source)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}