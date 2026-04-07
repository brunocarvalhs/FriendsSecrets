package br.com.brunocarvalhs.group.list.app.presentation

import androidx.compose.runtime.Stable
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel

@Stable
data class GroupListUiState(
    val isLoading: Boolean = false,
    val list: List<GroupModel> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedTag: String = "Todos"
) {
    val tags: List<String> = listOf("Todos", "Ativos", "Finalizados", "Sorteados")

    val filteredList: List<GroupModel>
        get() {
            val currentTime = System.currentTimeMillis()
            return list.filter { group ->
                val matchesSearch = group.name.contains(searchQuery, ignoreCase = true)
                val groupDateLong = group.date?.toLongOrNull() ?: 0L
                val matchesTag = when (selectedTag) {
                    "Ativos" -> groupDateLong >= currentTime
                    "Finalizados" -> groupDateLong < currentTime
                    "Sorteados" -> group.draws.isNotEmpty()
                    else -> true
                }
                matchesSearch && matchesTag
            }
        }
}