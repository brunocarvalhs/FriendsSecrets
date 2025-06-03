package br.com.brunocarvalhs.friendssecrets.common.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Base64
import java.io.ByteArrayOutputStream

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

fun Uri.toDecodeBase64(): ByteArray? {
    return try {
        val str = this.toString()
        if (!str.matches(Regex("^[A-Za-z0-9+/=]+$"))) return null
        Base64.decode(str, Base64.NO_WRAP)
    } catch (e: Exception) {
        null
    }
}

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