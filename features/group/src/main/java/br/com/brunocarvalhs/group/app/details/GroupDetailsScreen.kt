package br.com.brunocarvalhs.group.app.details

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.data.model.create
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.ui.components.EditMemberBottomSheet
import br.com.brunocarvalhs.friendssecrets.ui.components.SuccessComponent
import br.com.brunocarvalhs.friendssecrets.ui.fake.toFake
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.group.R
import br.com.brunocarvalhs.group.app.details.components.FloatingButtonComponent
import br.com.brunocarvalhs.group.app.details.components.GroupDetailsContentComponent
import br.com.brunocarvalhs.group.app.details.components.GroupDetailsErrorComponent
import br.com.brunocarvalhs.group.app.details.components.GroupDetailsLoadingComponent
import br.com.brunocarvalhs.group.app.details.components.HeaderComponent
import br.com.brunocarvalhs.group.commons.navigation.GroupEditScreenRoute
import br.com.brunocarvalhs.group.commons.navigation.GroupRevelationScreenRoute
import kotlin.collections.set

@Composable
fun GroupDetailsScreen(
    navController: NavController = rememberNavController(),
    viewModel: GroupDetailsViewModel,
    groupId: String,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.eventIntent(GroupDetailsIntent.FetchGroup(groupId))
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is GroupDetailsUiState.Draw -> {
                viewModel.eventIntent(GroupDetailsIntent.FetchGroup(groupId))
            }

            is GroupDetailsUiState.Exit -> {
                navController.popBackStack()
            }
        }
    }

    GroupDetailsContent(
        groupId = groupId,
        navController = navController,
        uiState = uiState,
        onEvent = viewModel::eventIntent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupDetailsContent(
    groupId: String = "",
    navController: NavController,
    uiState: GroupDetailsUiState,
    onEvent: (GroupDetailsIntent) -> Unit = {},
) {
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var name by remember { mutableStateOf("") }
    var likes by remember { mutableStateOf(listOf<String>()) }

    fun onDraw(group: GroupEntities) {
        onEvent.invoke(GroupDetailsIntent.DrawMembers(group = group))
    }

    fun exitGroup(group: GroupEntities) {
        onEvent.invoke(GroupDetailsIntent.ExitGroup(group.id))
    }

    fun deleteGroup(group: GroupEntities) {
        onEvent.invoke(GroupDetailsIntent.DeleteGroup(group.id))
    }

    fun revelationDraw(group: GroupEntities) {
        val destination = GroupRevelationScreenRoute(group.id)
        navController.navigate(destination)
    }

    fun onShare(participant: UserEntities, secret: String, token: String) {
        onEvent.invoke(
            GroupDetailsIntent.ShareMember(
                context = context, member = participant, secret = secret, token = token
            )
        )
    }

    fun onEdit(group: GroupEntities, participant: UserEntities) {
        onEvent.invoke(
            GroupDetailsIntent.EditMember(
                group = group,
                participant = participant,
            )
        )
    }

    fun onRemove(group: GroupEntities, participant: UserEntities) {
        onEvent.invoke(GroupDetailsIntent.RemoveMember(group = group, participant = participant))
    }

    fun onShareGroup(group: GroupEntities) {
        onEvent.invoke(GroupDetailsIntent.ShareGroup(context = context, group = group))
    }

    Scaffold(topBar = {
        HeaderComponent(
            uiState = uiState,
            navController = navController,
            scrollBehavior = scrollBehavior,
            exitGroup = ::exitGroup,
            deleteGroup = ::deleteGroup,
            onDraw = ::onDraw,
            onShareGroup = ::onShareGroup
        )
    }) { paddingValue ->
        when (uiState) {
            is GroupDetailsUiState.Error -> {
                GroupDetailsErrorComponent(
                    modifier = Modifier.padding(paddingValue),
                    navController = navController,
                    uiState = uiState
                )
            }

            GroupDetailsUiState.Loading -> {
                GroupDetailsLoadingComponent(
                    modifier = Modifier.padding(paddingValue)
                )
            }

            is GroupDetailsUiState.Success -> {
                GroupDetailsContentComponent(
                    modifier = Modifier
                        .padding(paddingValue)
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    uiState = uiState,
                    scrollBehavior = scrollBehavior,
                    showBottomSheet = showBottomSheet,
                    setShowBottomSheet = { showBottomSheet = it },
                    setName = { name = it },
                    setLikes = { likes = it },
                    onEdit = { group -> navController.navigate(route = GroupEditScreenRoute(group.id)) },
                    onShare = ::onShare,
                    onRemove = ::onRemove,
                    onDraw = ::onDraw,
                    onShareGroup = ::onShareGroup
                )
            }
        }
    }
    if (showBottomSheet && uiState is GroupDetailsUiState.Success) {
        EditMemberBottomSheet(onDismiss = { showBottomSheet = false },
            member = UserEntities.create(
                name = name,
                likes = likes
            ),
            onMemberAdded = { member ->
                onEdit(
                    group = uiState.group,
                    participant = member,
                )
            })
    }
    when (uiState) {
        is GroupDetailsUiState.Draw -> {
            SuccessComponent(redirectTo = {
                onEvent.invoke(GroupDetailsIntent.FetchGroup(groupId))
            })
        }
    }
}


@Composable
fun ExpandableText(
    text: String,
    maxLines: Int = 3,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
            style = MaterialTheme.typography.bodyMedium
        )
        if (text.length > 100) {
            TextButton(onClick = { isExpanded = !isExpanded }) {
                Text(
                    stringResource(
                        id = if (isExpanded) R.string.group_details_text_button_label_see_less
                        else R.string.group_details_text_button_label_see_more
                    )
                )
            }
        }
    }
}

internal class GroupDetailsPreviewProvider : PreviewParameterProvider<GroupDetailsUiState> {
    override val values = sequenceOf(
        GroupDetailsUiState.Loading,
        GroupDetailsUiState.Error(message = "Error"),
        // Members ----------------------------------------------------------------
        GroupDetailsUiState.Success(
            group = GroupEntities.toFake(name = "Group",
                description = "Description",
                members = listOf<UserEntities>().apply {
                    repeat(10) {
                        UserEntities.toFake(
                            name = "Member $it",
                            likes = listOf("Like $it")
                        )
                    }
                })
        ),
        GroupDetailsUiState.Success(
            group = GroupEntities.toFake(
                name = "Group sorteado",
                description = "Description",
                members = listOf<UserEntities>().apply {
                    repeat(10) {
                        UserEntities.toFake(
                            name = "Member $it",
                            likes = listOf("Like $it")
                        )
                    }
                },
                draws = mutableMapOf<String, String>().apply {
                    repeat(10) {
                        this["Member $it"] = "Secret Santa $it"
                    }
                },
            )
        ),

        // Admin -----------------------------------------------------------------
        GroupDetailsUiState.Success(
            group = GroupEntities.toFake(name = "Group admin",
                isOwner = true,
                description = "Description",
                members = listOf<UserEntities>().apply {
                    repeat(10) {
                        UserEntities.toFake(
                            name = "Member $it",
                            likes = listOf("Like $it")
                        )
                    }
                }),
        ),
        GroupDetailsUiState.Success(
            group = GroupEntities.toFake(
                name = "Group admin sorteado",
                isOwner = true,
                description = "Description",
                members = listOf<UserEntities>().apply {
                    repeat(10) {
                        UserEntities.toFake(
                            name = "Member $it",
                            likes = listOf("Like $it")
                        )
                    }
                },
                draws = mutableMapOf<String, String>().apply {
                    repeat(10) {
                        this["Member $it"] = "Secret Santa $it"
                    }
                },
            ),
        ),
    )
}

@Composable
@Preview(
    name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO
)
fun GroupDetailsScreenPreview(
    @PreviewParameter(GroupDetailsPreviewProvider::class) state: GroupDetailsUiState,
) {
    FriendsSecretsTheme {
        GroupDetailsContent(
            navController = rememberNavController(),
            uiState = state,
            onEvent = {}
        )
    }
}