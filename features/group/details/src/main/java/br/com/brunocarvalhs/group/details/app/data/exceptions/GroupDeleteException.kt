package br.com.brunocarvalhs.group.details.app.data.exceptions

internal data class GroupDeleteException(
    override val message: String = "Error deleting group"
) : Exception(message)
