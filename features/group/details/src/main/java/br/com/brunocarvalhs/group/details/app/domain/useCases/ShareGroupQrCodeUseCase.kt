package br.com.brunocarvalhs.group.details.app.domain.useCases

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.data.services.QrCodeGenerator
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

internal class ShareGroupQrCodeUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val qrCodeGenerator: QrCodeGenerator,
) {
    operator fun invoke(group: GroupModel): Result<Unit> = runCatching {
        val bitmap = qrCodeGenerator.generate(group.token) ?: error("Could not generate QR code")
        val uri = writeToCache(bitmap, group.token)

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val chooser = Intent.createChooser(sendIntent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(chooser)
    }

    private fun writeToCache(bitmap: Bitmap, token: String): android.net.Uri {
        val directory = File(context.cacheDir, "qr_codes").apply { mkdirs() }
        val file = File(directory, "qr_$token.png")
        FileOutputStream(file).use { output ->
            bitmap.compress(Bitmap.CompressFormat.PNG, QUALITY, output)
        }
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.group.details.fileprovider",
            file
        )
    }

    private companion object {
        const val QUALITY = 100
    }
}
