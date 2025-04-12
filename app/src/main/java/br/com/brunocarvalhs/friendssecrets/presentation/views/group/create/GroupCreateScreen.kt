package br.com.brunocarvalhs.friendssecrets.presentation.views.group.create

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.AddMemberBottomSheet
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.ContactItem
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.SuccessComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GroupCreateScreen(
    navController: NavController = rememberNavController(),
    viewModel: GroupCreateViewModel = viewModel(factory = GroupCreateViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val contactPermissionState = rememberPermissionState(
        android.Manifest.permission.READ_CONTACTS
    )

    LaunchedEffect(Unit) {
        if (!contactPermissionState.status.isGranted) {
            contactPermissionState.launchPermissionRequest()
        } else {
            viewModel.eventIntent(GroupCreateIntent.FetchContacts(context))
        }
    }

    GroupCreateContent(navController = navController, uiState = uiState, onHome = {
        navController.popBackStack()
    }, onBack = {
        viewModel.eventIntent(
            intent = GroupCreateIntent.Back
        )
    }, onSave = { name, description, members ->
        viewModel.eventIntent(
            intent = GroupCreateIntent.CreateGroup(
                name = name, description = description, members = members
            )
        )
    })
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
    val contacts = remember { mutableStateListOf<UserEntities>() }
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
                    name.text, description.text,
                    // mesclas os membros selecionados com os contatos selecionados
                    members.toMap() + contacts.associateBy({ it.name },
                        { it.likes.orEmpty().joinToString("|") })
                )
            }) {
                Icon(Icons.Filled.Check, stringResource(R.string.group_create_action_save_group))
                Text(stringResource(R.string.group_create_action_save_group))
            }
        }
    }) { paddingValue ->
        when (uiState) {
            is GroupCreateUiState.Idle -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValue)
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
                                Icon(
                                    Icons.Filled.Add,
                                    stringResource(R.string.group_create_text_button_member)
                                )
                            }
                        }
                    }
                    items(members.keys.toList()) { member ->
                        ContactItem(
                            contact = UserModel(
                                name = member,
                                likes = members[member]?.split("|") ?: emptyList(),
                            ),
                            isSelected = true,
                            action = { _, _ ->
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete contact",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable {
                                            members.remove(member)
                                        }
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                        )
                    }
                    if (uiState.contacts.isNotEmpty()) {
                        items(uiState.contacts
                            .filterNot { contact -> members.containsKey(contact.name) }
                        ) { contact ->
                            ContactItem(
                                contact = contact,
                                isSelected = contacts.contains(contact),
                                action = { _, isLiked ->

                                    if (contact.likes.any { it.isNotBlank() }) {
                                        Icon(
                                            imageVector = if (isLiked) Icons.Sharp.KeyboardArrowUp else Icons.Sharp.KeyboardArrowDown,
                                            contentDescription = "Toggle Likes"
                                        )
                                    }

                                    Checkbox(
                                        checked = contacts.contains(contact),
                                        onCheckedChange = { isChecked ->
                                            if (isChecked)
                                                contacts.add(contact)
                                            else
                                                contacts.remove(contact)
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }

            GroupCreateUiState.Loading -> {
                LoadingProgress(
                    modifier = Modifier
                        .padding(paddingValue)
                        .fillMaxSize()
                )
            }
        }
    }
    if (showBottomSheet && uiState is GroupCreateUiState.Idle) {
        AddMemberBottomSheet(onDismiss = { showBottomSheet = false },
            onMemberAdded = { member, likes -> members[member] = likes.joinToString("|") })
    }
    when (uiState) {
        is GroupCreateUiState.Success -> {
            SuccessComponent(modifier = Modifier.fillMaxSize(),
                redirectTo = { navController.popBackStack() })
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
        GroupCreateUiState.Idle(
            contacts = listOf<UserEntities>(
                UserModel(
                    name = "Produto de Teste",
                    id = "1",
                    phoneNumber = "123456789",
                    photoUrl = "",
                    isPhoneNumberVerified = false,
                    likes = listOf("Produto 1", "Produto 2")
                ),
                UserModel(
                    name = "Produto de Teste",
                    id = "1",
                    phoneNumber = "123456789",
                    photoUrl = "",
                    isPhoneNumberVerified = false,
                    likes = listOf("Produto 1", "Produto 2")
                ),
                UserModel(
                    name = "Produto de Teste",
                    id = "1",
                    phoneNumber = "123456789",
                    photoUrl = "",
                    isPhoneNumberVerified = false,
                    likes = listOf("Produto 1", "Produto 2")
                ),
            )
        ),
        GroupCreateUiState.Loading,
        GroupCreateUiState.Success,
        GroupCreateUiState.Error(message = "Error"),
    )
}

@Composable
@Preview(
    name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO
)
@Preview(showBackground = true)
fun GroupCreateScreenPreview(
    @PreviewParameter(GroupCreatePreviewProvider::class) state: GroupCreateUiState,
) {
    FriendsSecretsTheme {
        GroupCreateContent(navController = rememberNavController(),
            uiState = state,
            onBack = { },
            onHome = { },
            onSave = { _, _, _ -> })
    }
}
