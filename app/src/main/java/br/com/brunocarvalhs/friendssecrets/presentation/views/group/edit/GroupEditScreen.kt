package br.com.brunocarvalhs.friendssecrets.presentation.views.group.edit

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.AddMemberBottomSheet
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.MemberItem
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.SuccessComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.HomeNavigation

@Composable
fun GroupEditScreen(
    navController: NavController = rememberNavController(),
    viewModel: GroupEditViewModel = viewModel(
        factory = GroupEditViewModel.Factory
    ),
    groupId: String,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.eventIntent(GroupEditIntent.FetchGroup(groupId))
    }

    GroupEditContent(
        navController = navController,
        uiState = uiState,
        onHome = {
            navController.navigate(HomeNavigation.Home.route) {
                popUpTo(HomeNavigation.Home.route) {
                    inclusive = true
                }
            }
        },
        onBack = {
            navController.popBackStack()
        },
        onEdit = { group ->
            viewModel.eventIntent(
                intent = GroupEditIntent.EditGroup(group)
            )
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GroupEditContent(
    navController: NavController,
    uiState: GroupEditUiState,
    onHome: () -> Unit,
    onBack: () -> Unit,
    onEdit: (GroupEntities) -> Unit,
) {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var description by remember { mutableStateOf(TextFieldValue()) }
    val members = remember { mutableStateMapOf<String, String>() }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {

            },
            navigationIcon = {
                NavigationBackIconButton(navController = navController)
            },
        )
    }, floatingActionButton = {
        if (uiState is GroupEditUiState.Idle) {
            ExtendedFloatingActionButton(onClick = {
                onEdit.invoke(uiState.group.toCopy(
                    name = name.text,
                    description = description.text,
                    members = members.toMap()
                ))
            }) {
                Icon(Icons.Filled.Check, stringResource(R.string.group_create_action_save_group))
                Text(stringResource(R.string.group_create_action_save_group))
            }
        }
    }) {
        when (uiState) {
            is GroupEditUiState.Idle -> {
                GroupEditForm(
                    modifier = Modifier.padding(it),
                    uiState = uiState,
                    name = name,
                    onNameChange = { name = it },
                    description = description,
                    onDescriptionChange = { description = it },
                    members = members,
                    onMembersChange = {
                        members.clear()
                        members.putAll(it)
                    }
                )
            }

            GroupEditUiState.Loading -> {
                LoadingProgress(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                )
            }
        }
    }

    when (uiState) {
        is GroupEditUiState.Success -> {
            SuccessComponent(
                modifier = Modifier.fillMaxSize(),
                redirectTo = { navController.popBackStack() }
            )
        }

        is GroupEditUiState.Error -> {
            ErrorComponent(
                modifier = Modifier.fillMaxSize(),
                message = uiState.message,
                onBack = onHome,
                onRefresh = onBack
            )
        }
    }
}

@Composable
private fun GroupEditForm(
    uiState: GroupEditUiState.Idle,
    modifier: Modifier = Modifier,
    name: TextFieldValue,
    onNameChange: (TextFieldValue) -> Unit = {},
    description: TextFieldValue,
    onDescriptionChange: (TextFieldValue) -> Unit = {},
    members: SnapshotStateMap<String, String>,
    onMembersChange: (Map<String, String>) -> Unit = { _ -> }
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onNameChange(TextFieldValue(uiState.group.name))
        onDescriptionChange(TextFieldValue(uiState.group.description.orEmpty()))
        onMembersChange(uiState.group.members)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Row(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text(text = stringResource(R.string.group_create_input_name)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }
        item {
            Row(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text(text = stringResource(R.string.group_create_input_description)) },
                    modifier = Modifier.weight(1f)
                )
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
                TextButton(onClick = { showBottomSheet = true }) {
                    Text(text = stringResource(R.string.group_create_text_button_member))
                }
                IconButton(onClick = { showBottomSheet = true }) {
                    Icon(
                        Icons.Filled.Add,
                        stringResource(R.string.group_create_text_button_member)
                    )
                }
            }
        }
        items(members.keys.toList()) { member ->
            MemberItem(
                participant = member,
                likes = members[member]?.split("|") ?: emptyList(),
                onRemove = {
                    onMembersChange(
                        members.toMap().toMutableMap().apply { remove(member) })
                },
            )
            HorizontalDivider()
        }
    }
    if (showBottomSheet) {
        AddMemberBottomSheet(
            onDismiss = { showBottomSheet = false },
            onMemberAdded = { member, likes -> members[member] = likes.joinToString("|") }
        )
    }
}


private class GroupEditPreviewProvider : PreviewParameterProvider<GroupEditUiState> {
    override val values = sequenceOf(
        GroupEditUiState.Idle(
            group = GroupModel(
                id = "1",
                name = "Group",
                description = "Description",
                members = mapOf(
                    "1" to "Likes"
                )
            )
        ),
        GroupEditUiState.Loading,
        GroupEditUiState.Success,
        GroupEditUiState.Error(message = "Error")
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
@Preview(showBackground = true)
fun GroupCreateScreenPreview(
    @PreviewParameter(GroupEditPreviewProvider::class) state: GroupEditUiState,
) {
    FriendsSecretsTheme {
        GroupEditContent(
            navController = rememberNavController(),
            uiState = state,
            onBack = { },
            onHome = { },
            onEdit = { _ -> }
        )
    }
}