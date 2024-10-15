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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.text.style.TextAlign
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
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.MemberItem
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.SuccessComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.generative.GenerativeNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.GroupNavigation
import kotlinx.coroutines.delay

@Composable
fun GroupDetailsScreen(
    navController: NavController = rememberNavController(),
    viewModel: GroupDetailsViewModel = viewModel(
        factory = GroupDetailsViewModel.Factory
    ),
    groupId: String,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventIntent(GroupDetailsIntent.FetchGroup(groupId))
    }

    LaunchedEffect(Unit) {
        if (uiState is GroupDetailsUiState.Draw) {
            delay(timeMillis = 1000)
            viewModel.eventIntent(GroupDetailsIntent.FetchGroup(groupId))
        }
    }

    GroupDetailsContent(
        navController = navController,
        uiState = uiState,
        onShare = { participant, secret, token ->
            viewModel.eventIntent(
                intent = GroupDetailsIntent.ShareMember(
                    context = context,
                    member = participant,
                    secret = secret,
                    token = token
                )
            )
        },
        onDraw = {
            viewModel.eventIntent(
                intent = GroupDetailsIntent.DrawMembers(group = it)
            )
        },
        onRefresh = {
            viewModel.eventIntent(GroupDetailsIntent.FetchGroup(groupId))
        },
        onAI = { description, participant, likes ->
            val prompt = context.getString(R.string.ai_prompt, participant, likes, description)
            navController.navigate(
                route = GenerativeNavigation.Chat.createRoute(prompt)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupDetailsContent(
    navController: NavController,
    uiState: GroupDetailsUiState,
    onShare: (String, String, String) -> Unit = { _, _, _ -> },
    onDraw: (GroupEntities) -> Unit = {},
    onRefresh: () -> Unit = {},
    onAI: (String, String, String) -> Unit = { _, _, _ -> },
) {
    var expanded by remember { mutableStateOf(false) }

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    if (uiState is GroupDetailsUiState.Success) {
                        Text(uiState.group.name)
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More"
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        if (uiState is GroupDetailsUiState.Success && uiState.group.isOwner) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.group_details_drop_menu_item_text_edit)) },
                                onClick = {
                                    navController.navigate(
                                        route = GroupNavigation.Edit.createRoute(uiState.group.id)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Edit,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.group_details_drop_menu_item_text_settings)) },
                            onClick = { /* Handle settings! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Settings,
                                    contentDescription = null
                                )
                            }
                        )
                        if (uiState is GroupDetailsUiState.Success && uiState.group.isOwner) {
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.group_details_action_draw_members)) },
                                onClick = { onDraw.invoke(uiState.group) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Refresh,
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = { Text("F11", textAlign = TextAlign.Center) }
                            )
                        }
                    }
                },
                navigationIcon = {
                    NavigationBackIconButton(navController = navController)
                },
                scrollBehavior = scrollBehavior
            )
        }, floatingActionButton = {
            if (uiState is GroupDetailsUiState.Success) {
                if (uiState.group.draws.isNotEmpty()) {
                    ExtendedFloatingActionButton(onClick = { onDraw.invoke(uiState.group) }) {
                        Icon(Icons.Filled.People, "my secret friend")
                        Text(stringResource(R.string.group_details_action_preview_my_secret_friend))
                    }
                } else if (uiState.group.isOwner && uiState.group.draws.isEmpty()) {
                    ExtendedFloatingActionButton(onClick = { onDraw.invoke(uiState.group) }) {
                        Icon(Icons.Filled.Refresh, "draw")
                        Text(stringResource(R.string.group_details_action_draw_members))
                    }
                }
            }
        }) {
        when (uiState) {
            is GroupDetailsUiState.Error -> {
                ErrorComponent(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    message = uiState.message,
                    onBack = { navController.popBackStack() }
                )
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
                            Row {
                                Text(text = "Descrição")
                            }
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                ExpandableText(
                                    text = uiState.group.description?.textWithFormatting()
                                        .orEmpty(),
                                    maxLines = 3
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
                            Text(text = "Membros")
                        }
                    }
                    if (uiState.group.draws.isNotEmpty()) {
                        items(uiState.group.draws.keys.toList()) { participant ->
                            MemberItem(
                                participant = participant,
                                group = uiState.group,
                                isAdministrator = uiState.group.isOwner,
                                onShare = onShare,
                                likes = uiState.group.members[participant]?.split("|").orEmpty(),
                                onAI = onAI
                            )
                            HorizontalDivider()
                        }
                    } else if (uiState.group.members.isNotEmpty()) {
                        items(uiState.group.members.keys.toList()) { member ->
                            MemberItem(
                                participant = member,
                                group = uiState.group,
                                isAdministrator = uiState.group.isOwner,
                                likes = uiState.group.members[member]?.split("|").orEmpty()
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
    when (uiState) {
        is GroupDetailsUiState.Draw -> {
            SuccessComponent(redirectTo = onRefresh)
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
            group = GroupModel(
                name = "Group",
                description = "Description",
                members = mutableMapOf<String, String>().apply {
                    repeat(10) {
                        this["Member $it"] = "Secret Santa $it"
                    }
                }
            )
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
            group = GroupModel(
                name = "Group admin",
                isOwner = true,
                description = "Description",
                members = mutableMapOf<String, String>().apply {
                    repeat(10) {
                        this["Member $it"] = "Secret Santa $it"
                    }
                }
            ),
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
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
fun GroupDetailsScreenPreview(
    @PreviewParameter(GroupDetailsPreviewProvider::class) state: GroupDetailsUiState,
) {
    FriendsSecretsTheme {
        GroupDetailsContent(
            navController = rememberNavController(),
            uiState = state,
            onShare = { _, _, _ -> },
            onDraw = { }
        )
    }
}