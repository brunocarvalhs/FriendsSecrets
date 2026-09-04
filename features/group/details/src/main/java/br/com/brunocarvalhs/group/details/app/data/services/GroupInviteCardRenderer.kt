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
            intArrayOf(Color.parseColor(BACKGROUND_TOP_COLOR), Color.parseColor(BACKGROUND_BOTTOM_COLOR)),
            null,
            Shader.TileMode.CLAMP
        )
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { shader = gradient }
        canvas.drawRect(0f, 0f, WIDTH.toFloat(), HEIGHT.toFloat(), paint)
    }

    private fun drawHeader(canvas: Canvas) {
        val paint = textPaint(size = HEADER_TEXT_SIZE, bold = true)
        canvas.drawText("🎁 Amigo Secreto", WIDTH / 2f, HEADER_Y, paint)
    }

    private fun drawGroupName(canvas: Canvas, name: String) {
        val paint = textPaint(size = GROUP_NAME_TEXT_SIZE, bold = true)
        canvas.drawText(name, WIDTH / 2f, GROUP_NAME_Y, paint)
    }

    private fun drawSubtitle(canvas: Canvas, group: GroupModel) {
        val paint = textPaint(size = SUBTITLE_TEXT_SIZE, bold = false, alpha = SUBTITLE_ALPHA)
        val subtitle = "Você foi convidado a participar!"
        canvas.drawText(subtitle, WIDTH / 2f, SUBTITLE_Y, paint)

        group.date?.let {
            val datePaint = textPaint(size = DATE_TEXT_SIZE, bold = false, alpha = DATE_ALPHA)
            canvas.drawText("📅 $it", WIDTH / 2f, DATE_Y, datePaint)
        }
    }

    private fun drawQrCode(canvas: Canvas, token: String) {
        val qrBitmap = qrCodeGenerator.generate(token, size = QR_SIZE) ?: return
        val left = (WIDTH - QR_SIZE) / 2f
        val top = QR_TOP

        val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }
        val backgroundRect = RectF(
            left - QR_PADDING,
            top - QR_PADDING,
            left + QR_SIZE + QR_PADDING,
            top + QR_SIZE + QR_PADDING
        )
        canvas.drawRoundRect(backgroundRect, CARD_CORNER_RADIUS, CARD_CORNER_RADIUS, backgroundPaint)
        canvas.drawBitmap(qrBitmap, left, top, null)
    }

    private fun drawFooter(canvas: Canvas, token: String) {
        val tokenPaint = textPaint(size = TOKEN_TEXT_SIZE, bold = true)
        canvas.drawText(token, WIDTH / 2f, HEIGHT - TOKEN_Y_OFFSET, tokenPaint)

        val footerPaint = textPaint(size = FOOTER_TEXT_SIZE, bold = false, alpha = FOOTER_ALPHA)
        canvas.drawText(
            "Escaneie o QR ou digite o código no app",
            WIDTH / 2f,
            HEIGHT - FOOTER_Y_OFFSET,
            footerPaint
        )
    }

    private fun textPaint(size: Float, bold: Boolean, alpha: Int = FULL_ALPHA) =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
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
        const val QR_TOP = 600f
        const val CARD_CORNER_RADIUS = 32f

        const val BACKGROUND_TOP_COLOR = "#5B2A86"
        const val BACKGROUND_BOTTOM_COLOR = "#1F1147"

        const val FULL_ALPHA = 255

        const val HEADER_TEXT_SIZE = 64f
        const val HEADER_Y = 220f

        const val GROUP_NAME_TEXT_SIZE = 84f
        const val GROUP_NAME_Y = 360f

        const val SUBTITLE_TEXT_SIZE = 40f
        const val SUBTITLE_ALPHA = 200
        const val SUBTITLE_Y = 440f

        const val DATE_TEXT_SIZE = 36f
        const val DATE_ALPHA = 170
        const val DATE_Y = 500f

        const val TOKEN_TEXT_SIZE = 44f
        const val TOKEN_Y_OFFSET = 160f

        const val FOOTER_TEXT_SIZE = 32f
        const val FOOTER_ALPHA = 170
        const val FOOTER_Y_OFFSET = 100f
    }
}
