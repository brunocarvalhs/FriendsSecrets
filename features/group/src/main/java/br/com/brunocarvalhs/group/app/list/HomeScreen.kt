package br.com.brunocarvalhs.group.app.list

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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.common.navigation.GroupGraphRoute
import br.com.brunocarvalhs.friendssecrets.common.navigation.ProfileGraphRoute
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.ui.components.BottomNavItem
import br.com.brunocarvalhs.friendssecrets.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationComponent
import br.com.brunocarvalhs.friendssecrets.ui.fake.toFake
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.group.app.list.components.EmptyGroupComponent
import br.com.brunocarvalhs.group.app.list.components.GroupCard
import br.com.brunocarvalhs.group.app.list.components.GroupToEnterBottomSheet
import br.com.brunocarvalhs.group.app.list.components.HeaderHomeComponent
import br.com.brunocarvalhs.group.commons.navigation.GroupCreateScreenRoute
import br.com.brunocarvalhs.group.commons.navigation.GroupDetailsScreenRoute

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val session by viewModel.session.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event(HomeIntent.FetchGroups)
    }

    HomeContent(
        session = session,
        navController = navController,
        uiState = uiState,
        onEvent = viewModel::event,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    session: UserEntities? = null,
    navController: NavController,
    uiState: HomeUiState,
    onEvent: (HomeIntent) -> Unit = {},
    isJoinGroupEnabled: Boolean = true,
    isCreateGroupEnabled: Boolean = true,
) {
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Groups) }

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HeaderHomeComponent(
                session = session,
                scrollBehavior = scrollBehavior,
                onAdd = { navController.navigate(GroupCreateScreenRoute) },
                onNotification = { showBottomSheet = true },
            )
        },
        bottomBar = {
            NavigationComponent(
                selectedItem = selectedItem,
                onItemSelected = { menu ->
                    selectedItem = menu
                    val route: Any = when (menu) {
                        is BottomNavItem.Groups -> GroupGraphRoute
                        is BottomNavItem.Profile -> ProfileGraphRoute
                    }
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
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
                        onCreateGroup = { navController.navigate(GroupCreateScreenRoute) },
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
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                group = item,
                                onClick = {
                                    val destination = GroupDetailsScreenRoute(item.id)
                                    navController.navigate(destination)
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
            GroupEntities.toFake(
                name = "Group $it",
                description = "Description $it",
                members = listOf<UserEntities>().apply {
                    repeat(10) {
                        UserEntities.toFake(
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
            session = UserEntities.toFake(
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

