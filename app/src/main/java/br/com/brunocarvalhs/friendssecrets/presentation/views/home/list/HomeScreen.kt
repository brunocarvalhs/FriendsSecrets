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
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import br.com.brunocarvalhs.friendssecrets.commons.extensions.isFistAppOpen
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleKeys
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.GroupNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.HomeNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.components.EmptyGroupComponent
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.components.GroupCard
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.components.GroupToEnterBottomSheet
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.components.MenuHome
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.components.MenuItem

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory
    ),
    toggleManager: ToggleManager,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val session = SessionManager.getInstance()

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
        session = session.getCurrentUserModel(),
        navController = navController,
        uiState = uiState,
        onEvent = viewModel::event,
        isSettingsEnabled = toggleManager
            .isFeatureEnabled(ToggleKeys.SETTINGS_IS_ENABLED),
        isJoinGroupEnabled = toggleManager
            .isFeatureEnabled(ToggleKeys.HOME_IS_JOIN_GROUP_ENABLED),
        isCreateGroupEnabled = toggleManager
            .isFeatureEnabled(ToggleKeys.HOME_IS_CREATE_GROUP_ENABLED),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    session: UserEntities? = null,
    navController: NavController,
    uiState: HomeUiState,
    onEvent: (HomeIntent) -> Unit = {},
    isSettingsEnabled: Boolean = true,
    isJoinGroupEnabled: Boolean = true,
    isCreateGroupEnabled: Boolean = true,
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var expanded by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), // Adicionado aqui
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    session?.let {
                        Text(
                            text = "${stringResource(R.string.home_title)} ${it.name}",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    if (isSettingsEnabled || isJoinGroupEnabled) {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "More"
                            )
                        }
                        MenuHome(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            onClick = {
                                when (val menu = it) {
                                    MenuItem.JoinGroup -> showBottomSheet = true
                                    MenuItem.Logout -> {
                                        onEvent(HomeIntent.Logout)
                                        navController.navigate(HomeNavigation.Home.route) {
                                            popUpTo(HomeNavigation.Home.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                    else -> navController.navigate(menu.route.orEmpty())
                                }
                            },
                        )
                    }
                },
                scrollBehavior = scrollBehavior // Associar comportamento de rolagem
            )
        },
        floatingActionButton = {
            if (uiState is HomeUiState.Success) {
                if (uiState.list.isEmpty()) return@Scaffold

                if (isCreateGroupEnabled) {
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
                    onRefresh = { onEvent(HomeIntent.FetchGroups) }
                )
            }

            is HomeUiState.Success -> {
                if (uiState.list.isEmpty()) {
                    EmptyGroupComponent(
                        modifier = Modifier.padding(it),
                        onGroupToEnter = { showBottomSheet = true },
                        onCreateGroup = { navController.navigate(GroupNavigation.Create.route) },
                        isJoinGroupEnabled = isJoinGroupEnabled,
                        isCreateGroupEnabled = isCreateGroupEnabled
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection), // Adicionado aqui
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
            onToEnter = { onEvent(HomeIntent.GroupToEnter(it)) }
        )
    }
}

private class HomePreviewProvider : PreviewParameterProvider<HomeUiState> {
    override val values = sequenceOf(
        HomeUiState.Loading,
        HomeUiState.Success(list = listOf()),
        HomeUiState.Success(list = (1..10).map { it ->
            GroupModel(
                name = "Group $it",
                description = "Description $it",
                members = listOf<UserEntities>().apply {
                    repeat(10) {
                        UserModel(
                            name = "Member $it",
                            likes = listOf("Like $it")
                        )
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
fun HomeContentSessionPreview(
    @PreviewParameter(HomePreviewProvider::class) state: HomeUiState,
) {
    FriendsSecretsTheme {
        HomeContent(
            session = UserModel(
                id = "1",
                name = "John Doe",
                photoUrl = null,
                phoneNumber = "+5511999999999",
                isPhoneNumberVerified = true
            ),
            navController = rememberNavController(),
            uiState = state
        )
    }
}

