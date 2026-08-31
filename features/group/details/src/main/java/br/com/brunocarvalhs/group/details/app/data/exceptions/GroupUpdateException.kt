package br.com.brunocarvalhs.group.details.app.data.exceptions

internal data class GroupUpdateException(
    override val message: String = "Error updating group"
) : Exception(message)
