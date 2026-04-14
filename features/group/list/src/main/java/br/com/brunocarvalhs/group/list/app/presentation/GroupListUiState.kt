package br.com.brunocarvalhs.group.list.app.presentation

import androidx.compose.runtime.Stable
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

private const val DATE_FORMAT = "dd/MM/yyyy"
private const val EMPTY_STRING = ""

internal enum class GroupFilterTag(val description: String) {
    ACTIVE("Ativos"),
    ARCHIVED("Arquivados"),
    DRAWN("Sorteados"),
    NOT_DRAWN("Não sorteados")
}

@Stable
internal data class GroupListUiState(
    val isLoading: Boolean = false,
    val list: List<GroupModel> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = EMPTY_STRING,
    val selectedTag: GroupFilterTag = GroupFilterTag.ACTIVE
) {
    val tags: List<GroupFilterTag> = GroupFilterTag.entries

    val filteredList: List<GroupModel>
        get() {
            val currentTime = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

            return list.filter { group ->
                val matchesSearch = group.name.contains(searchQuery, ignoreCase = true)
                
                val groupDateLong = try {
                    group.date?.let { dateFormat.parse(it)?.time } ?: Long.MAX_VALUE
                } catch (e: Exception) {
                    Timber.e(e, "Error parsing date: %s", group.date)
                    0L
                }

                val matchesTag = when (selectedTag) {
                    GroupFilterTag.ARCHIVED -> groupDateLong < currentTime
                    GroupFilterTag.DRAWN -> group.draws.isNotEmpty()
                    GroupFilterTag.NOT_DRAWN -> group.draws.isEmpty()
                    GroupFilterTag.ACTIVE -> groupDateLong >= currentTime
                }
                matchesSearch && matchesTag
            }
        }
}
