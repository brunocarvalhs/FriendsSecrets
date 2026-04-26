package br.com.brunocarvalhs.group.create.app.data.exceptions

internal data class FailedCreateGroupException(
    override val message: String = "Failed to create group"
): Exception(message)
