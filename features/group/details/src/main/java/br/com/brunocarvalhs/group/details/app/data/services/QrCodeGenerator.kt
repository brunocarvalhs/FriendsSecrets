package br.com.brunocarvalhs.group.details.app.data.services

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import javax.inject.Inject

internal class QrCodeGenerator @Inject constructor() {

    fun generate(content: String, size: Int = DEFAULT_SIZE): Bitmap? = runCatching {
        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        bitmap
    }.getOrNull()

    private companion object {
        const val DEFAULT_SIZE = 512
    }
}
