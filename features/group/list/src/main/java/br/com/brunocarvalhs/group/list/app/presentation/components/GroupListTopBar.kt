package br.com.brunocarvalhs.group.list.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.list.R
import br.com.brunocarvalhs.group.list.app.presentation.GroupFilterTag
import br.com.brunocarvalhs.group.list.commons.options.OptionsMore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GroupListTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedTag: GroupFilterTag,
    onTagSelect: (GroupFilterTag) -> Unit,
    tags: List<GroupFilterTag>,
    onJoinGroupClick: () -> Unit,
    modifier: Modifier = Modifier.Companion,
    moreOptions: List<OptionsMore> = emptyList(),
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 4.dp
    ) {
        Column {
            GroupListAppBar(
                scrollBehavior = scrollBehavior,
                onJoinGroupClick = onJoinGroupClick,
                moreOptions = moreOptions,
                expanded = expanded,
                onExpandedChange = { expanded = it }
            )

            GroupListSearchField(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange
            )

            GroupListFilterRow(
                tags = tags,
                selectedTag = selectedTag,
                onTagSelect = onTagSelect
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupListAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onJoinGroupClick: () -> Unit,
    moreOptions: List<OptionsMore>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
) {
    TopAppBar(
        title = { Text("Friends Secrets", style = MaterialTheme.typography.titleLarge) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Companion.Transparent
        ),
        actions = {
            IconButton(onClick = onJoinGroupClick) {
                Icon(
                    Icons.Default.GroupAdd,
                    contentDescription = stringResource(R.string.join_group)
                )
            }

            if (moreOptions.isNotEmpty()) {
                IconButton(onClick = { onExpandedChange(true) }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.more_options)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) }
                ) {
                    moreOptions.forEach { option ->
                        DropdownMenuItem(
                            leadingIcon = {
                                option.icon?.let {
                                    Icon(
                                        imageVector = option.icon,
                                        contentDescription = option.contentDescription()
                                    )
                                }
                            },
                            text = { Text(text = option.name()) },
                            onClick = {
                                onExpandedChange(false)
                                option.lambda()
                            }
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun GroupListSearchField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text(stringResource(R.string.search_groups)) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        shape = MaterialTheme.shapes.medium,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        )
    )
}

@Composable
private fun GroupListFilterRow(
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
            FilterChip(
                selected = selectedTag == tag,
                onClick = { onTagSelect(tag) },
                label = { Text(stringResource(tag.description)) }
            )
        }
    }
}
