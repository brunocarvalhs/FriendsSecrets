package br.com.brunocarvalhs.group.create.app.presentation.forms

sealed interface FormsIntent {
    data class CreateGroup(val onFinish: (String) -> Unit) : FormsIntent
    data class UpdateName(val name: String): FormsIntent
}