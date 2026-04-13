package br.com.brunocarvalhs.group.create.app.data.exceptions

data class FailedCreateGroupException(
    override val message: String = "Failed to create group"
): Exception(message)
