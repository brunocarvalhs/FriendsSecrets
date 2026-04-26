package br.com.brunocarvalhs.group.list.app.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.list.R
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

private const val DATE_FORMAT = "dd/MM/yyyy"
private const val EMPTY_STRING = ""

internal enum class GroupFilterTag(@field:StringRes val description: Int) {
    ACTIVE(R.string.group_list_filter_active),
    ARCHIVED(R.string.group_list_filter_archived),
    DRAWN(R.string.group_list_filter_drawn),
    NOT_DRAWN(R.string.group_list_filter_not_drawn)
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
                } catch (e: java.text.ParseException) {
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
