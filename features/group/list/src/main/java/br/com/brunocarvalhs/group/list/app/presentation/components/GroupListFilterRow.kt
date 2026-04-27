package br.com.brunocarvalhs.group.list.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.list.app.presentation.GroupFilterTag

@Composable
internal fun GroupListFilterRow(
    tags: List<GroupFilterTag>,
    selectedTag: GroupFilterTag,
    onTagSelect: (GroupFilterTag) -> Unit
) {
    LazyRow(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tags) { tag ->
            val description = stringResource(tag.description)
            FilterChip(
                modifier = Modifier.semantics {
                    contentDescription = description
                    selected = (selectedTag == tag)
                    role = Role.Tab
                },
                selected = selectedTag == tag,
                onClick = { onTagSelect(tag) },
                label = { Text(description) }
            )
        }
    }
}
