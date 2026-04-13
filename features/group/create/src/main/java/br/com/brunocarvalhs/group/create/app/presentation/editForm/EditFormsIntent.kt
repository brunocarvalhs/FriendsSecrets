package br.com.brunocarvalhs.group.create.app.presentation.editForm

sealed interface EditFormsIntent {
    data class SaveGroup(val onFinish: () -> Unit) : EditFormsIntent
    data class UpdateName(val name: String): EditFormsIntent
    data class UpdateDescription(val description: String): EditFormsIntent
    data class UpdateDate(val date: String): EditFormsIntent
    data class UpdateMinPrice(val minPrice: String): EditFormsIntent
    data class UpdateMaxPrice(val maxPrice: String): EditFormsIntent
    data class UpdatePhoto(val photoUrl: String?): EditFormsIntent
}