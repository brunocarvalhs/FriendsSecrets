package br.com.brunocarvalhs.group.draw.app.data.exceptions

internal data class FailedDrawException(
    override val message: String = "Failed to save the draw"
): Exception(message)
