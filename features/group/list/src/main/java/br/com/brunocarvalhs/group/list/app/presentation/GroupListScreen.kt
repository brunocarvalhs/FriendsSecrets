package br.com.brunocarvalhs.group.list.app.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.list.R
import br.com.brunocarvalhs.group.list.app.presentation.components.EmptyGroupComponent
import br.com.brunocarvalhs.group.list.app.presentation.components.ErrorComponent
import br.com.brunocarvalhs.group.list.app.presentation.components.GroupCard
import br.com.brunocarvalhs.group.list.app.presentation.components.GroupToEnterBottomSheet
import br.com.brunocarvalhs.group.list.app.presentation.components.LoadingProgress
import br.com.brunocarvalhs.group.list.commons.options.OptionsMore

@Composable
internal fun GroupListScreen(
    viewModel: GroupListViewModel,
    onGroupToCreate: () -> Unit = { },
    onGroupToEnter: (GroupModel) -> Unit = { },
    moreOptions: List<OptionsMore> = emptyList(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(GroupListIntent.FetchGroups)
    }

    ListContent(
        isLoading = uiState.isLoading,
        list = uiState.filteredList,
        errorMessage = uiState.errorMessage,
        searchQuery = uiState.searchQuery,
        selectedTag = uiState.selectedTag,
        tags = uiState.tags,
        onSearchQueryChange = { viewModel.handleEvent(GroupListIntent.OnSearchQueryChange(it)) },
        onTagSelected = { viewModel.handleEvent(GroupListIntent.OnTagSelected(it)) },
        onFetchGroups = { viewModel.handleEvent(GroupListIntent.FetchGroups) },
        onGroupToEnter = onGroupToEnter,
        onGroupToCreate = onGroupToCreate,
        onJoinGroup = { token ->
            viewModel.handleEvent(GroupListIntent.GroupToEnter(token))
        },
        moreOptions = moreOptions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListContent(
    isLoading: Boolean,
    list: List<GroupModel>,
    errorMessage: String?,
    searchQuery: String,
    selectedTag: GroupFilterTag,
    tags: List<GroupFilterTag>,
    onSearchQueryChange: (String) -> Unit,
    onTagSelected: (GroupFilterTag) -> Unit,
    onFetchGroups: () -> Unit = {},
    onGroupToEnter: (GroupModel) -> Unit = {},
    onGroupToCreate: () -> Unit = {},
    onJoinGroup: (String) -> Unit = {},
    moreOptions: List<OptionsMore> = emptyList(),
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            GroupListTopBar(
                scrollBehavior = scrollBehavior,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                selectedTag = selectedTag,
                onTagSelected = onTagSelected,
                tags = tags,
                onJoinGroupClick = { showBottomSheet = true },
                moreOptions = moreOptions
            )
        },
        floatingActionButton = {
            GroupListFab(onGroupToCreate = onGroupToCreate)
        }
    ) { paddingValues ->
        GroupListContent(
            modifier = Modifier.padding(paddingValues),
            isLoading = isLoading,
            list = list,
            errorMessage = errorMessage,
            onFetchGroups = onFetchGroups,
            onGroupToEnter = onGroupToEnter,
            onJoinGroupClick = { showBottomSheet = true },
            onGroupToCreate = onGroupToCreate
        )
    }

    if (showBottomSheet) {
        GroupToEnterBottomSheet(
            onDismiss = { showBottomSheet = false },
            onToEnter = onJoinGroup
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupListTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedTag: GroupFilterTag,
    onTagSelected: (GroupFilterTag) -> Unit,
    tags: List<GroupFilterTag>,
    onJoinGroupClick: () -> Unit,
    moreOptions: List<OptionsMore> = emptyList(),
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 4.dp
    ) {
        Column {
            TopAppBar(
                title = { Text("Friends Secrets", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
                    IconButton(onClick = onJoinGroupClick) {
                        Icon(Icons.Default.GroupAdd, contentDescription = stringResource(R.string.join_group))
                    }

                    if (moreOptions.isNotEmpty()) {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.more_options))
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
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
                                        expanded = false
                                        option.lambda()
                                    }
                                )
                            }
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
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

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tags) { tag ->
                    FilterChip(
                        selected = selectedTag == tag,
                        onClick = { onTagSelected(tag) },
                        label = { Text(stringResource(tag.description)) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupListContent(
    isLoading: Boolean,
    list: List<GroupModel>,
    errorMessage: String?,
    onFetchGroups: () -> Unit,
    onGroupToEnter: (GroupModel) -> Unit,
    onJoinGroupClick: () -> Unit,
    onGroupToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = onFetchGroups,
        modifier = modifier.fillMaxSize()
    ) {
        when {
            isLoading && list.isEmpty() -> {
                LoadingProgress()
            }

            errorMessage != null && list.isEmpty() -> {
                ErrorComponent(
                    message = errorMessage,
                    onRefresh = onFetchGroups
                )
            }

            else -> {
                if (list.isEmpty() && !isLoading) {
                    EmptyGroupComponent(
                        onGroupToEnter = onJoinGroupClick,
                        onCreateGroup = onGroupToCreate
                    )
                } else {
                    GroupListItems(
                        groups = list,
                        onGroupToEnter = onGroupToEnter
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupListItems(
    groups: List<GroupModel>,
    onGroupToEnter: (GroupModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(groups) { item ->
            GroupCard(
                modifier = Modifier.fillMaxWidth(),
                name = item.name,
                description = item.description,
                date = item.date,
                membersCount = item.members.size,
                onClick = { onGroupToEnter(item) }
            )
        }
    }
}

@Composable
private fun GroupListFab(
    onGroupToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onGroupToCreate,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.new_group)
        )
    }
}

private class HomePreviewProvider : PreviewParameterProvider<GroupListUiState> {
    override val values = sequenceOf(
        GroupListUiState(isLoading = true),
        GroupListUiState(list = emptyList()),
        GroupListUiState(
            list = (1..10).map { i ->
                GroupModel(
                    name = "Group $i",
                    description = "Description $i",
                    members = List(10) {
                        UserModel(
                            name = "Member $it",
                            likes = listOf("Like $it")
                        )
                    }
                )
            }
        ),
        GroupListUiState(errorMessage = "Error")
    )
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
private fun ListContentPreview(
    @PreviewParameter(HomePreviewProvider::class) state: GroupListUiState,
) {
    ListContent(
        isLoading = state.isLoading,
        list = state.filteredList,
        errorMessage = state.errorMessage,
        searchQuery = state.searchQuery,
        selectedTag = state.selectedTag,
        tags = state.tags,
        onSearchQueryChange = {},
        onTagSelected = {}
    )
}
