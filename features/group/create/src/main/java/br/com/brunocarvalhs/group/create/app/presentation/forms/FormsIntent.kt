package br.com.brunocarvalhs.group.create.app.presentation.forms

internal sealed interface FormsIntent {
    data class CreateGroup(val onFinish: (String) -> Unit) : FormsIntent
    data class UpdateName(val name: String): FormsIntent
    data class UpdateDescription(val description: String): FormsIntent
    data class UpdateDate(val date: String): FormsIntent
    data class UpdateMinPrice(val minPrice: String): FormsIntent
    data class UpdateMaxPrice(val maxPrice: String): FormsIntent
    data class UpdatePhoto(val photoUrl: String?): FormsIntent
}