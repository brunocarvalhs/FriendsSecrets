package br.com.brunocarvalhs.group.list.app.presentation.list

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.LifecycleResumeEffect
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.list.app.domain.entities.UserModel
import br.com.brunocarvalhs.group.list.app.presentation.list.components.EmptyGroupComponent
import br.com.brunocarvalhs.group.list.app.presentation.list.components.ErrorComponent
import br.com.brunocarvalhs.group.list.app.presentation.list.components.GroupCard
import br.com.brunocarvalhs.group.list.app.presentation.list.components.GroupToEnterBottomSheet
import br.com.brunocarvalhs.group.list.app.presentation.list.components.HeaderHomeComponent
import br.com.brunocarvalhs.group.list.app.presentation.list.components.LoadingProgress

@Composable
fun GroupListScreen(
    viewModel: GroupListViewModel,
    onGroupToCreate: () -> Unit = { },
    onGroupToEnter: (GroupModel) -> Unit = { },
) {
    val uiState by viewModel.uiState.collectAsState()

    LifecycleResumeEffect(Unit) {
        viewModel.event(GroupListIntent.FetchGroups)
        onPauseOrDispose { }
    }

    ListContent(
        uiState = uiState,
        onFetchGroups = { viewModel.event(GroupListIntent.FetchGroups) },
        onGroupToEnter = onGroupToEnter,
        onGroupToCreate = onGroupToCreate,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListContent(
    uiState: GroupListUiState,
    onFetchGroups: () -> Unit = {},
    onGroupToEnter: (GroupModel) -> Unit = {},
    onGroupToCreate: () -> Unit = {},
    isJoinGroupEnabled: Boolean = true,
    isCreateGroupEnabled: Boolean = true,
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HeaderHomeComponent(
                scrollBehavior = scrollBehavior,
                onAdd = onGroupToCreate,
                onNotification = { showBottomSheet = true },
            )
        }
    ) {
        when (uiState) {
            is GroupListUiState.Error -> {
                ErrorComponent(
                    modifier = Modifier.padding(it),
                    message = uiState.errorMessage,
                    onRefresh = onFetchGroups
                )
            }

            is GroupListUiState.Success -> {
                if (uiState.list.isEmpty()) {
                    EmptyGroupComponent(
                        modifier = Modifier.padding(it),
                        onGroupToEnter = { showBottomSheet = true },
                        onCreateGroup = onGroupToCreate,
                        isJoinGroupEnabled = isJoinGroupEnabled,
                        isCreateGroupEnabled = isCreateGroupEnabled
                    )
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        columns = GridCells.Fixed(2),
                    ) {
                        items(uiState.list) { item ->
                            GroupCard(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                name = item.name,
                                onClick = {
                                    onGroupToEnter(item)
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.size(200.dp))
                        }
                    }
                }
            }

            GroupListUiState.Loading -> LoadingProgress(modifier = Modifier.fillMaxSize())
        }
    }
    if (showBottomSheet) {
        GroupToEnterBottomSheet(
            onDismiss = { showBottomSheet = false },
            onToEnter = { /* Handle entering by ID if needed */ }
        )
    }
}

private class HomePreviewProvider : PreviewParameterProvider<GroupListUiState> {
    override val values = sequenceOf(
        GroupListUiState.Loading,
        GroupListUiState.Success(list = listOf()),
        GroupListUiState.Success(list = (1..10).map { it ->
            GroupModel(
                id = it.toString(),
                name = "Group $it",
                description = "Description $it",
                token = "Token $it",
                members = listOf<UserModel>().apply {
                    repeat(10) {
                        UserModel(
                            name = "Member $it",
                            likes = listOf("Like $it")
                        )
                    }
                }
            )
        }),
        GroupListUiState.Error(errorMessage = "Error")
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
fun ListContentPreview(
    @PreviewParameter(HomePreviewProvider::class) state: GroupListUiState,
) {
    ListContent(
        uiState = state
    )
}
