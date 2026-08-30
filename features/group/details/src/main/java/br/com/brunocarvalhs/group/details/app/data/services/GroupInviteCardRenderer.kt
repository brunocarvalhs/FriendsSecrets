package br.com.brunocarvalhs.group.details.app.data.services

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import br.com.brunocarvalhs.core.domain.model.GroupModel
import javax.inject.Inject

internal class GroupInviteCardRenderer @Inject constructor(
    private val qrCodeGenerator: QrCodeGenerator,
) {

    fun render(group: GroupModel): Bitmap {
        val bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawBackground(canvas)
        drawHeader(canvas)
        drawGroupName(canvas, group.name.ifBlank { "Amigo Secreto" })
        drawSubtitle(canvas, group)
        drawQrCode(canvas, group.token)
        drawFooter(canvas, group.token)

        return bitmap
    }

    private fun drawBackground(canvas: Canvas) {
        val gradient = LinearGradient(
            0f, 0f, 0f, HEIGHT.toFloat(),
            intArrayOf(Color.parseColor("#5B2A86"), Color.parseColor("#1F1147")),
            null,
            Shader.TileMode.CLAMP
        )
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { shader = gradient }
        canvas.drawRect(0f, 0f, WIDTH.toFloat(), HEIGHT.toFloat(), paint)
    }

    private fun drawHeader(canvas: Canvas) {
        val paint = textPaint(size = 64f, bold = true)
        canvas.drawText("🎁 Amigo Secreto", WIDTH / 2f, 220f, paint)
    }

    private fun drawGroupName(canvas: Canvas, name: String) {
        val paint = textPaint(size = 84f, bold = true)
        canvas.drawText(name, WIDTH / 2f, 360f, paint)
    }

    private fun drawSubtitle(canvas: Canvas, group: GroupModel) {
        val paint = textPaint(size = 40f, bold = false, alpha = 200)
        val subtitle = "Você foi convidado a participar!"
        canvas.drawText(subtitle, WIDTH / 2f, 440f, paint)

        group.date?.let {
            val datePaint = textPaint(size = 36f, bold = false, alpha = 170)
            canvas.drawText("📅 $it", WIDTH / 2f, 500f, datePaint)
        }
    }

    private fun drawQrCode(canvas: Canvas, token: String) {
        val qrBitmap = qrCodeGenerator.generate(token, size = QR_SIZE) ?: return
        val left = (WIDTH - QR_SIZE) / 2f
        val top = 600f

        val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }
        val backgroundRect = RectF(
            left - QR_PADDING,
            top - QR_PADDING,
            left + QR_SIZE + QR_PADDING,
            top + QR_SIZE + QR_PADDING
        )
        canvas.drawRoundRect(backgroundRect, 32f, 32f, backgroundPaint)
        canvas.drawBitmap(qrBitmap, left, top, null)
    }

    private fun drawFooter(canvas: Canvas, token: String) {
        val tokenPaint = textPaint(size = 44f, bold = true)
        canvas.drawText(token, WIDTH / 2f, HEIGHT - 160f, tokenPaint)

        val footerPaint = textPaint(size = 32f, bold = false, alpha = 170)
        canvas.drawText("Escaneie o QR ou digite o código no app", WIDTH / 2f, HEIGHT - 100f, footerPaint)
    }

    private fun textPaint(size: Float, bold: Boolean, alpha: Int = 255) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        this.alpha = alpha
        textSize = size
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, if (bold) Typeface.BOLD else Typeface.NORMAL)
    }

    private companion object {
        const val WIDTH = 1080
        const val HEIGHT = 1350
        const val QR_SIZE = 480
        const val QR_PADDING = 24f
    }
}
