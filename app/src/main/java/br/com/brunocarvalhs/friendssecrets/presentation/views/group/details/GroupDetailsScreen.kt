package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
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
import br.com.brunocarvalhs.friendssecrets.commons.extensions.textWithFormatting
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.EditMemberBottomSheet
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.MemberItem
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.SuccessComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.GroupNavigation
import kotlinx.coroutines.delay
import kotlin.collections.set

@Composable
fun GroupDetailsScreen(
    navController: NavController = rememberNavController(),
    viewModel: GroupDetailsViewModel = viewModel(
        factory = GroupDetailsViewModel.Factory
    ),
    groupId: String,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.eventIntent(GroupDetailsIntent.FetchGroup(groupId))
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is GroupDetailsUiState.Draw -> {
                delay(timeMillis = 1000)
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
    var expanded by remember { mutableStateOf(false) }
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
        navController.navigate(route = GroupNavigation.Revelation.createRoute(group.id))
    }

    fun onShare(participant: String, secret: String, token: String) {
        onEvent.invoke(
            GroupDetailsIntent.ShareMember(
                context = context, member = participant, secret = secret, token = token
            )
        )
    }

    fun onEdit(group: GroupEntities, participant: String, likes: List<String>) {
        onEvent.invoke(
            GroupDetailsIntent.EditMember(
                group = group,
                participant = participant,
                likes = likes
            )
        )
    }

    fun onRemove(group: GroupEntities, participant: String) {
        onEvent.invoke(GroupDetailsIntent.RemoveMember(group = group, participant = participant))
    }

    fun onShareGroup(group: GroupEntities) {
        onEvent.invoke(GroupDetailsIntent.ShareGroup(context = context, group = group))
    }

    Scaffold(topBar = {
        LargeTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {
            if (uiState is GroupDetailsUiState.Success) {
                Text(uiState.group.name)
            }
        }, actions = {
            if (uiState is GroupDetailsUiState.Success) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert, contentDescription = "More"
                    )
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(text = { Text(stringResource(R.string.group_details_drop_menu_item_text_edit)) },
                        onClick = {
                            navController.navigate(
                                route = GroupNavigation.Edit.createRoute(uiState.group.id)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Edit, contentDescription = null
                            )
                        })
                    DropdownMenuItem(text = {
                        if (uiState.group.isOwner) {
                            Text(stringResource(R.string.group_details_drop_menu_item_text_exit_to_group_admin))
                        } else {
                            Text(stringResource(R.string.group_details_drop_menu_item_text_exit_to_group))
                        }
                    }, onClick = {
                        if (uiState.group.isOwner) {
                            deleteGroup(uiState.group)
                        } else {
                            exitGroup(uiState.group)
                        }
                    }, leadingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = if (uiState.group.isOwner) {
                                stringResource(R.string.group_details_drop_menu_item_text_exit_to_group_admin)
                            } else {
                                stringResource(R.string.group_details_drop_menu_item_text_exit_to_group)
                            }
                        )
                    })
                    if (uiState.group.isOwner) {
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.group_details_action_draw_members)) },
                            onClick = { onDraw(group = uiState.group) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = stringResource(R.string.group_details_action_draw_members)
                                )
                            },
                        )
                    }
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.group_details_action_shared)) },
                        onClick = { onShareGroup(group = uiState.group) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = stringResource(R.string.group_details_action_shared)
                            )
                        },
                    )
                }
            }

        }, navigationIcon = {
            NavigationBackIconButton(navController = navController)
        }, scrollBehavior = scrollBehavior
        )
    }, floatingActionButton = {
        if (uiState is GroupDetailsUiState.Success) {
            if (uiState.group.draws.isNotEmpty()) {
                ExtendedFloatingActionButton(onClick = { revelationDraw(uiState.group) }) {
                    Icon(
                        imageVector = Icons.Filled.People,
                        contentDescription = stringResource(R.string.group_details_action_preview_my_secret_friend)
                    )
                    Text(text = stringResource(R.string.group_details_action_preview_my_secret_friend))
                }
            } else if (uiState.group.isOwner && uiState.group.draws.isEmpty()) {
                ExtendedFloatingActionButton(onClick = { onDraw(uiState.group) }) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = stringResource(R.string.group_details_action_draw_members)
                    )
                    Text(text = stringResource(R.string.group_details_action_draw_members))
                }
            }
        }
    }) {
        when (uiState) {
            is GroupDetailsUiState.Error -> {
                ErrorComponent(modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                    message = uiState.message,
                    onBack = { navController.popBackStack() })
            }

            GroupDetailsUiState.Loading -> {
                LoadingProgress(
                    modifier = Modifier.fillMaxSize()
                )
            }

            is GroupDetailsUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                ) {
                    item {
                        uiState.group.description?.let {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.group_details_description),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                ExpandableText(
                                    text = uiState.group.description?.textWithFormatting()
                                        .orEmpty(), maxLines = 3
                                )
                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(R.string.group_details_members))
                        }
                    }
                    if (uiState.group.draws.isNotEmpty()) {
                        items(uiState.group.draws.keys.toList()) { participant ->
                            MemberItem(
                                participant = participant,
                                group = uiState.group,
                                isAdministrator = uiState.group.isOwner,
                                onRemove = {
                                    onRemove(
                                        group = uiState.group,
                                        participant = participant
                                    )
                                },
                                onEdit = {
                                    showBottomSheet = !showBottomSheet
                                    name = participant
                                    likes = uiState.group.members[participant]?.split("|")
                                        .orEmpty()
                                },
                                onShare = { member, secret, token ->
                                    onShare(
                                        participant = member,
                                        secret = secret,
                                        token = token
                                    )
                                },
                                likes = uiState.group.members[participant]?.split("|")
                                    .orEmpty()
                            )
                            HorizontalDivider()
                        }
                    } else if (uiState.group.members.isNotEmpty()) {
                        items(uiState.group.members.keys.toList()) { member ->
                            MemberItem(
                                participant = member,
                                group = uiState.group,
                                isAdministrator = uiState.group.isOwner,
                                likes = uiState.group.members[member]?.split("|").orEmpty(),
                                onEdit = {
                                    showBottomSheet = !showBottomSheet
                                    name = member
                                    likes = uiState.group.members[member]?.split("|")
                                        .orEmpty()
                                },
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
    if (showBottomSheet && uiState is GroupDetailsUiState.Success) {
        EditMemberBottomSheet(
            onDismiss = { showBottomSheet = false },
            member = name,
            likes = likes,
            onMemberAdded = { member, likes ->
                onEdit(
                    group = uiState.group,
                    participant = member,
                    likes = likes
                )
            }
        )
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

private class GroupDetailsPreviewProvider : PreviewParameterProvider<GroupDetailsUiState> {
    override val values = sequenceOf(
        GroupDetailsUiState.Loading,
        GroupDetailsUiState.Error(message = "Error"),
        // Members ----------------------------------------------------------------
        GroupDetailsUiState.Success(
            group = GroupModel(name = "Group",
                description = "Description",
                members = mutableMapOf<String, String>().apply {
                    repeat(10) {
                        this["Member $it"] = "Secret Santa $it"
                    }
                })
        ),
        GroupDetailsUiState.Success(
            group = GroupModel(
                name = "Group sorteado",
                description = "Description",
                members = mutableMapOf<String, String>().apply {
                    repeat(10) {
                        this["Member $it"] = "Secret Santa $it"
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
            group = GroupModel(name = "Group admin",
                isOwner = true,
                description = "Description",
                members = mutableMapOf<String, String>().apply {
                    repeat(10) {
                        this["Member $it"] = "Secret Santa $it"
                    }
                }),
        ),
        GroupDetailsUiState.Success(
            group = GroupModel(
                name = "Group admin sorteado",
                isOwner = true,
                description = "Description",
                members = mutableMapOf<String, String>().apply {
                    repeat(10) {
                        this["Member $it"] = "Secret Santa $it"
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
            onEvent = {

            })
    }
}