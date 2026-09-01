package br.com.brunocarvalhs.group.list.app.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.group.list.app.presentation.components.EmptyGroupComponent
import br.com.brunocarvalhs.group.list.app.presentation.components.ErrorComponent
import br.com.brunocarvalhs.group.list.app.presentation.components.GroupListAppBar
import br.com.brunocarvalhs.group.list.app.presentation.components.GroupListFab
import br.com.brunocarvalhs.group.list.app.presentation.components.GroupListFilterRow
import br.com.brunocarvalhs.group.list.app.presentation.components.GroupListItems
import br.com.brunocarvalhs.group.list.app.presentation.components.GroupListSearchField
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
        uiState = uiState,
        onSearchQueryChange = { viewModel.handleEvent(GroupListIntent.OnSearchQueryChange(it)) },
        onTagSelect = { viewModel.handleEvent(GroupListIntent.OnTagSelected(it)) },
        onFetchGroups = { viewModel.handleEvent(GroupListIntent.FetchGroups) },
        onGroupToEnter = onGroupToEnter,
        onGroupToCreate = onGroupToCreate,
        onJoinGroup = { token ->
            viewModel.handleEvent(GroupListIntent.GroupToEnter(token))
        },
        onJoinGroupOpen = { viewModel.handleEvent(GroupListIntent.JoinGroupStarted) },
        moreOptions = moreOptions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListContent(
    uiState: GroupListUiState,
    onSearchQueryChange: (String) -> Unit,
    onTagSelect: (GroupFilterTag) -> Unit,
    onFetchGroups: () -> Unit,
    onGroupToEnter: (GroupModel) -> Unit,
    onGroupToCreate: () -> Unit,
    onJoinGroup: (String) -> Unit,
    onJoinGroupOpen: () -> Unit,
    moreOptions: List<OptionsMore>,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showBottomSheet by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 4.dp
            ) {
                Column {
                    GroupListAppBar(
                        scrollBehavior = scrollBehavior,
                        onJoinGroupClick = { showBottomSheet = true; onJoinGroupOpen() },
                        isJoinGroupEnabled = uiState.isJoinGroupEnabled,
                        moreOptions = moreOptions,
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    )

                    GroupListSearchField(
                        searchQuery = uiState.searchQuery,
                        onSearchQueryChange = onSearchQueryChange
                    )

                    GroupListFilterRow(
                        tags = uiState.tags,
                        selectedTag = uiState.selectedTag,
                        onTagSelect = onTagSelect
                    )
                }
            }
        },
        floatingActionButton = {
            if (uiState.isCreateGroupEnabled) {
                GroupListFab(onGroupToCreate = onGroupToCreate)
            }
        }
    ) { paddingValues ->
        GroupListContent(
            uiState = uiState,
            onFetchGroups = onFetchGroups,
            onGroupToEnter = onGroupToEnter,
            onJoinGroupClick = { showBottomSheet = true; onJoinGroupOpen() },
            onGroupToCreate = onGroupToCreate,
            modifier = Modifier.padding(paddingValues)
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
private fun GroupListContent(
    uiState: GroupListUiState,
    onFetchGroups: () -> Unit,
    onGroupToEnter: (GroupModel) -> Unit,
    onJoinGroupClick: () -> Unit,
    onGroupToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = onFetchGroups,
        modifier = modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading && uiState.filteredList.isEmpty() -> {
                LoadingProgress()
            }

            uiState.errorMessage != null && uiState.filteredList.isEmpty() -> {
                ErrorComponent(
                    message = uiState.errorMessage,
                    onRefresh = onFetchGroups
                )
            }

            else -> {
                if (uiState.filteredList.isEmpty() && !uiState.isLoading) {
                    EmptyGroupComponent(
                        onGroupToEnter = onJoinGroupClick,
                        onCreateGroup = onGroupToCreate,
                        isJoinGroupEnabled = uiState.isJoinGroupEnabled,
                        isCreateGroupEnabled = uiState.isCreateGroupEnabled
                    )
                } else {
                    GroupListItems(
                        groups = uiState.filteredList,
                        onGroupToEnter = onGroupToEnter
                    )
                }
            }
        }
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
        uiState = state,
        onSearchQueryChange = {},
        onTagSelect = {},
        onFetchGroups = {},
        onGroupToEnter = {},
        onGroupToCreate = {},
        onJoinGroup = {},
        onJoinGroupOpen = {},
        moreOptions = emptyList()
    )
}
