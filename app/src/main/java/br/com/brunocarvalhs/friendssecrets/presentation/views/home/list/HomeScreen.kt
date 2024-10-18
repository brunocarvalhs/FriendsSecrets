package br.com.brunocarvalhs.friendssecrets.presentation.views.home.list

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsEvents
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsParams
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.commons.utils.isFistAppOpen
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.presentation.Screen
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.GroupNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.HomeNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.components.EmptyGroupComponent
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.components.GroupCard
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.components.GroupToEnterBottomSheet

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory
    ),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        AnalyticsProvider.track(
            event = AnalyticsEvents.VISUALIZATION,
            params = mapOf(
                AnalyticsParams.SCREEN_NAME to HomeNavigation.Home.route
            )
        )
    }

    LaunchedEffect(Unit) {
        if (context.isFistAppOpen()) navController.navigate(HomeNavigation.Onboarding.route)
        viewModel.event(HomeIntent.FetchGroups)
    }

    HomeContent(
        navController = navController,
        uiState = uiState,
        onRefresh = { viewModel.event(HomeIntent.FetchGroups) },
        onGroupToEnter = { viewModel.event(HomeIntent.GroupToEnter(it)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    navController: NavController,
    uiState: HomeUiState,
    onRefresh: () -> Unit = {},
    onGroupToEnter: (String) -> Unit = {},
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var expanded by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {

                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More"
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.home_drop_menu_item_text_join_a_group)) },
                            onClick = { showBottomSheet = true },
                            leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.home_drop_menu_item_text_settings)) },
                            onClick = {
                                navController.navigate(
                                    route = Screen.Settings.route
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Settings,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            when (uiState) {
                is HomeUiState.Error, HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    if (uiState.list.isEmpty()) return@Scaffold

                    ExtendedFloatingActionButton(onClick = {
                        navController.navigate(GroupNavigation.Create.route)
                    }) {
                        Icon(Icons.Filled.Add, "Add")
                        Text(stringResource(R.string.home_action_create_group))
                    }
                }
            }
        }
    ) {
        when (uiState) {
            is HomeUiState.Error -> {
                ErrorComponent(
                    modifier = Modifier.padding(it),
                    message = uiState.errorMessage,
                    onRefresh = onRefresh
                )
            }

            is HomeUiState.Success -> {
                if (uiState.list.isEmpty()) {
                    EmptyGroupComponent(
                        modifier = Modifier.padding(it),
                        onGroupToEnter = {
                            showBottomSheet = true
                        },
                        onCreateGroup = {
                            navController.navigate(GroupNavigation.Create.route)
                        }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(uiState.list) { item ->
                            GroupCard(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                group = item,
                                onClick = {
                                    navController.navigate(
                                        route = GroupNavigation.Read.createRoute(item.id)
                                    )
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.size(200.dp))
                        }
                    }
                }
            }

            HomeUiState.Loading -> LoadingProgress(modifier = Modifier.fillMaxSize())
        }
    }
    if (showBottomSheet) {
        GroupToEnterBottomSheet(
            onDismiss = { showBottomSheet = false },
            onToEnter = { onGroupToEnter.invoke(it) }
        )
    }
}

private class HomePreviewProvider : PreviewParameterProvider<HomeUiState> {
    override val values = sequenceOf(
        HomeUiState.Loading,
        HomeUiState.Success(list = listOf()),
        HomeUiState.Success(list = (1..10).map {
            GroupModel(
                name = "Group $it",
                description = "Description $it",
                members = mutableMapOf<String, String>().apply {
                    repeat(10) {
                        this["Member $it"] = "Secret Santa $it"
                    }
                }
            )
        }),
        HomeUiState.Error(errorMessage = "Error")
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
fun HomeContentPreview(
    @PreviewParameter(HomePreviewProvider::class) state: HomeUiState,
) {
    FriendsSecretsTheme {
        HomeContent(
            navController = rememberNavController(),
            uiState = state
        )
    }
}
