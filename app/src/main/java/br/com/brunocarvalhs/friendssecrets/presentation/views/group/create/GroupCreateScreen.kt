package br.com.brunocarvalhs.friendssecrets.presentation.views.group.create

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.AddMemberBottomSheet
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.MemberItem
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.SuccessComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

@Composable
fun GroupCreateScreen(
    navController: NavController = rememberNavController(),
    viewModel: GroupCreateViewModel = viewModel(factory = GroupCreateViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()

    GroupCreateContent(
        navController = navController,
        uiState = uiState,
        onHome = {
            navController.popBackStack()
        },
        onBack = { viewModel.eventIntent(
            intent = GroupCreateIntent.Back
        ) },
        onSave = { name, description, members ->
            viewModel.eventIntent(
                intent = GroupCreateIntent.CreateGroup(
                    name = name,
                    description = description,
                    members = members
                )
            )
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GroupCreateContent(
    navController: NavController,
    uiState: GroupCreateUiState,
    onHome: () -> Unit,
    onBack: () -> Unit,
    onSave: (String, String, Map<String, String>) -> Unit,
) {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var description by remember { mutableStateOf(TextFieldValue()) }
    val members = remember { mutableStateMapOf<String, String>() }
    var showBottomSheet by remember { mutableStateOf(false) }

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
        if (uiState is GroupCreateUiState.Idle) {
            ExtendedFloatingActionButton(onClick = {
                onSave.invoke(
                    name.text,
                    description.text,
                    members
                )
            }) {
                Icon(Icons.Filled.Check, stringResource(R.string.group_create_action_save_group))
                Text(stringResource(R.string.group_create_action_save_group))
            }
        }
    }) {
        when (uiState) {
            GroupCreateUiState.Idle -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    item {
                        Row(modifier = Modifier.padding(16.dp)) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { value -> name = value },
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
                                onValueChange = { value -> description = value },
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
                                Icon(Icons.Filled.Add, stringResource(R.string.group_create_text_button_member))
                            }
                        }
                    }
                    items(members.keys.toList()) { member ->
                        MemberItem(
                            participant = member,
                            likes = members[member]?.split("|") ?: emptyList(),
                            onRemove = { members.remove(member) },
                        )
                        HorizontalDivider()
                    }
                }
            }

            GroupCreateUiState.Loading -> {
                LoadingProgress(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                )
            }
        }
    }
    if (showBottomSheet && uiState is GroupCreateUiState.Idle) {
        AddMemberBottomSheet(
            onDismiss = { showBottomSheet = false },
            onMemberAdded = { member, likes -> members[member] = likes.joinToString("|") }
        )
    }
    when (uiState) {
        is GroupCreateUiState.Success -> {
            SuccessComponent(
                modifier = Modifier.fillMaxSize(),
                redirectTo = { navController.popBackStack() }
            )
        }

        is GroupCreateUiState.Error -> {
            ErrorComponent(
                modifier = Modifier.fillMaxSize(),
                message = uiState.message,
                onBack = onHome,
                onRefresh = onBack
            )
        }
    }
}


private class GroupCreatePreviewProvider : PreviewParameterProvider<GroupCreateUiState> {
    override val values = sequenceOf(
        GroupCreateUiState.Idle,
        GroupCreateUiState.Loading,
        GroupCreateUiState.Success,
        GroupCreateUiState.Error(message = "Error")
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
    @PreviewParameter(GroupCreatePreviewProvider::class) state: GroupCreateUiState,
) {
    FriendsSecretsTheme {
        GroupCreateContent(
            navController = rememberNavController(),
            uiState = state,
            onBack = { },
            onHome = { },
            onSave = { _, _, _ -> }
        )
    }
}