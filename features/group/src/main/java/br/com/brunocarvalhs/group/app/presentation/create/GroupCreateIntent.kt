package br.com.brunocarvalhs.group.app.presentation.create

sealed class GroupCreateIntent {
    object FetchContacts : GroupCreateIntent()
    object CreateGroup : GroupCreateIntent()
    object ClearError : GroupCreateIntent()

    data class UpdateName(val value: String) : GroupCreateIntent()
    data class UpdateDescription(val value: String) : GroupCreateIntent()
    data class UpdateDrawDate(val value: String) : GroupCreateIntent()
    data class UpdateMinValue(val value: String) : GroupCreateIntent()
    data class UpdateMaxValue(val value: String) : GroupCreateIntent()
    data class UpdateDrawType(val value: String) : GroupCreateIntent()

    data class UpdateSearch(val value: String) : GroupCreateIntent()
    data class ToggleMember(val member: UserEntities) : GroupCreateIntent()
}
