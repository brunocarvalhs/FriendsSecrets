package br.com.brunocarvalhs.group.list.app.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.isFinished
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.list.app.presentation.components.GroupCard
import br.com.brunocarvalhs.group.list.app.presentation.list.components.EmptyGroupComponent
import br.com.brunocarvalhs.group.list.app.presentation.list.components.ErrorComponent
import br.com.brunocarvalhs.group.list.app.presentation.list.components.GroupToEnterBottomSheet
import br.com.brunocarvalhs.group.list.app.presentation.list.components.LoadingProgress

@Composable
fun GroupListScreen(
    viewModel: GroupListViewModel,
    onGroupToCreate: () -> Unit = { },
    onGroupToEnter: (GroupModel) -> Unit = { },
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(GroupListIntent.FetchGroups)
    }

    ListContent(
        uiState = uiState,
        onFetchGroups = { viewModel.handleEvent(GroupListIntent.FetchGroups) },
        onGroupToEnter = onGroupToEnter,
        onGroupToCreate = onGroupToCreate,
        onJoinGroup = { token ->
            viewModel.handleEvent(GroupListIntent.GroupToEnter(token))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListContent(
    uiState: GroupListUiState,
    onFetchGroups: () -> Unit = {},
    onGroupToEnter: (GroupModel) -> Unit = {},
    onGroupToCreate: () -> Unit = {},
    onJoinGroup: (String) -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showBottomSheet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf("Todos") }
    val tags = listOf("Todos", "Ativos", "Finalizados", "Sorteados")

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            GroupListTopBar(
                scrollBehavior = scrollBehavior,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                selectedTag = selectedTag,
                onTagSelected = { selectedTag = it },
                tags = tags,
                onJoinGroupClick = { showBottomSheet = true },
                onMoreOptionsClick = { /* TODO: Adicionar ação */ }
            )
        },
        floatingActionButton = {
            GroupListFab(onGroupToCreate = onGroupToCreate)
        }
    ) { paddingValues ->
        GroupListContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            searchQuery = searchQuery,
            selectedTag = selectedTag, // Adicione este parâmetro
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
    selectedTag: String,
    onTagSelected: (String) -> Unit,
    tags: List<String>,
    onJoinGroupClick: () -> Unit,
    onMoreOptionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                        Icon(Icons.Default.GroupAdd, contentDescription = "Entrar em Grupo")
                    }
                    IconButton(onClick = onMoreOptionsClick) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Mais opções")
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
                placeholder = { Text("Pesquisar grupos...") },
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
                items(tags.size) { index ->
                    val tag = tags[index]
                    FilterChip(
                        selected = selectedTag == tag,
                        onClick = { onTagSelected(tag) },
                        label = { Text(tag) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupListContent(
    uiState: GroupListUiState,
    searchQuery: String,
    selectedTag: String, // Adicionado
    onFetchGroups: () -> Unit,
    onGroupToEnter: (GroupModel) -> Unit,
    onJoinGroupClick: () -> Unit,
    onGroupToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = uiState is GroupListUiState.Loading,
        onRefresh = onFetchGroups,
        modifier = modifier.fillMaxSize()
    ) {
        when (uiState) {
            is GroupListUiState.Loading -> {
                LoadingProgress()
            }

            is GroupListUiState.Error -> {
                ErrorComponent(message = uiState.errorMessage, onRefresh = onFetchGroups)
            }

            is GroupListUiState.Success -> {
                val currentTime = System.currentTimeMillis()

                val filteredList = remember(uiState.list, searchQuery, selectedTag) {
                    uiState.list.filter { group ->
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

                if (filteredList.isEmpty()) {
                    EmptyGroupComponent(
                        onGroupToEnter = onJoinGroupClick,
                        onCreateGroup = onGroupToCreate
                    )
                } else {
                    GroupListItems(
                        groups = filteredList,
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
        Icon(Icons.Default.Add, contentDescription = "Novo Grupo")
    }
}

private class HomePreviewProvider : PreviewParameterProvider<GroupListUiState> {
    override val values = sequenceOf(
        GroupListUiState.Loading,
        GroupListUiState.Success(list = listOf()),
        GroupListUiState.Success(list = (1..10).map { it ->
            GroupModel(
                name = "Group $it",
                description = "Description $it",
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
