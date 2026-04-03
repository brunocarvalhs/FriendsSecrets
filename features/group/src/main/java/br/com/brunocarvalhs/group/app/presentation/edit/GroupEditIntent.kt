package br.com.brunocarvalhs.group.app.presentation.edit

sealed interface GroupEditIntent {
    data class FetchGroup(val id: String): GroupEditIntent
    data class EditGroup(val group: GroupEntities) : GroupEditIntent
}