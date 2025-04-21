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
import br.com.brunocarvalhs.friendssecrets.commons.extensions.isFistAppOpen
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleKeys
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.di.ServiceLocator
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
    toggleManager: ToggleManager = ServiceLocator.getToggleManager()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val session = SessionManager.getInstance()
    val analyticsProvider = ServiceLocator.getAnalyticsProvider()

    LaunchedEffect(Unit) {
        analyticsProvider.track(
            event = AnalyticsEvents.VISUALIZATION,
            params = mapOf(
                AnalyticsParams.SCREEN_NAME to HomeNavigation.Home.route
            )
        )
    }

    LaunchedEffect(Unit) {
        if (context.isFistAppOpen()) navController.navigate(HomeNavigation.Onboarding.route)
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
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(id = R.string.menu)
                        )
                    }
                    MenuHome(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        items = listOf(
                            MenuItem(
                                title = stringResource(id = R.string.settings),
                                onClick = {
                                    expanded = false
                                    navController.navigate(HomeNavigation.Settings.route)
                                },
                                isVisible = isSettingsEnabled
                            ),
                            MenuItem(
                                title = stringResource(id = R.string.logout),
                                onClick = {
                                    expanded = false
                                    onEvent(HomeIntent.Logout)
                                    navController.navigate(HomeNavigation.Login.route) {
                                        popUpTo(HomeNavigation.Home.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        )
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            if (isCreateGroupEnabled) {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(GroupNavigation.Create.route) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(id = R.string.create_group)
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.create_group)) },
                )
            }
        }
    ) { innerPadding ->
        when (uiState) {
            is HomeUiState.Loading -> {
                LoadingProgress(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            is HomeUiState.Success -> {
                if (uiState.list.isEmpty()) {
                    EmptyGroupComponent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        onCreateGroup = { navController.navigate(GroupNavigation.Create.route) },
                        onJoinGroup = { showBottomSheet = true },
                        isJoinGroupEnabled = isJoinGroupEnabled,
                        isCreateGroupEnabled = isCreateGroupEnabled
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                        items(uiState.list) { group ->
                            GroupCard(
                                group = group,
                                onClick = {
                                    navController.navigate(
                                        GroupNavigation.Details.createRoute(
                                            group.id
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            is HomeUiState.Error -> {
                ErrorComponent(
                    message = uiState.errorMessage,
                    onRetry = { onEvent(HomeIntent.FetchGroups) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }

        if (showBottomSheet && isJoinGroupEnabled) {
            GroupToEnterBottomSheet(
                onDismiss = { showBottomSheet = false },
                onConfirm = { token ->
                    showBottomSheet = false
                    onEvent(HomeIntent.GroupToEnter(token))
                }
            )
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomeUiStatePreviewParameterProvider::class) uiState: HomeUiState
) {
    FriendsSecretsTheme {
        HomeContent(
            session = UserModel(
                id = "1",
                name = "John Doe",
                email = "john@example.com",
                phone = "+1234567890",
                photo = null,
                likes = emptyList()
            ),
            navController = rememberNavController(),
            uiState = uiState
        )
    }
}

private class HomeUiStatePreviewParameterProvider : PreviewParameterProvider<HomeUiState> {
    override val values: Sequence<HomeUiState> = sequenceOf(
        HomeUiState.Loading,
        HomeUiState.Success(
            list = listOf(
                GroupModel(
                    id = "1",
                    name = "Amigo Secreto da Família",
                    description = "Amigo secreto para a reunião de família",
                    date = "25/12/2023",
                    value = "R$ 50,00",
                    members = listOf(
                        UserModel(
                            id = "1",
                            name = "John Doe",
                            email = "john@example.com",
                            phone = "+1234567890",
                            photo = null,
                            likes = emptyList()
                        ),
                        UserModel(
                            id = "2",
                            name = "Jane Doe",
                            email = "jane@example.com",
                            phone = "+1234567891",
                            photo = null,
                            likes = emptyList()
                        )
                    ),
                    token = "ABC123",
                    draws = emptyMap()
                ),
                GroupModel(
                    id = "2",
                    name = "Amigo Secreto do Trabalho",
                    description = "Amigo secreto para a confraternização do trabalho",
                    date = "20/12/2023",
                    value = "R$ 100,00",
                    members = listOf(
                        UserModel(
                            id = "1",
                            name = "John Doe",
                            email = "john@example.com",
                            phone = "+1234567890",
                            photo = null,
                            likes = emptyList()
                        ),
                        UserModel(
                            id = "3",
                            name = "Bob Smith",
                            email = "bob@example.com",
                            phone = "+1234567892",
                            photo = null,
                            likes = emptyList()
                        )
                    ),
                    token = "DEF456",
                    draws = emptyMap()
                )
            )
        ),
        HomeUiState.Success(list = emptyList()),
        HomeUiState.Error(errorMessage = "Ocorreu um erro ao carregar os grupos")
    )
}
