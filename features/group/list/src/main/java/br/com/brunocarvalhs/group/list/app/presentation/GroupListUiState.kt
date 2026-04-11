package br.com.brunocarvalhs.group.list.app.presentation

import androidx.compose.runtime.Stable
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import java.text.SimpleDateFormat
import java.util.Locale

@Stable
data class GroupListUiState(
    val isLoading: Boolean = false,
    val list: List<GroupModel> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedTag: String = "Todos"
) {
    val tags: List<String> = listOf("Ativos", "Arquivados", "Sorteados", "Não sorteados")

    val filteredList: List<GroupModel>
        get() {
            val currentTime = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            return list.filter { group ->
                val matchesSearch = group.name.contains(searchQuery, ignoreCase = true)
                
                val groupDateLong = try {
                    group.date?.let { dateFormat.parse(it)?.time } ?: Long.MAX_VALUE
                } catch (e: Exception) {
                    0L
                }

                val matchesTag = when (selectedTag) {
                    "Arquivados" -> groupDateLong < currentTime
                    "Sorteados" -> group.draws.isNotEmpty()
                    "Não sorteados" -> group.draws.isEmpty()
                    else -> groupDateLong >= currentTime
                }
                matchesSearch && matchesTag
            }
        }
}
