package br.com.brunocarvalhs.group.app.create

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.ui.components.ContactItem
import br.com.brunocarvalhs.friendssecrets.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.ui.components.SuccessComponent
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.group.R
import br.com.brunocarvalhs.group.commons.ui.components.AddMemberBottomSheet
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GroupCreateScreen(
    navController: NavController = rememberNavController(),
    viewModel: GroupCreateViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val contactPermissionState = rememberPermissionState(android.Manifest.permission.READ_CONTACTS)

    LaunchedEffect(Unit) {
        if (!contactPermissionState.status.isGranted) {
            contactPermissionState.launchPermissionRequest()
        } else {
            viewModel.eventIntent(GroupCreateIntent.FetchContacts)
        }
    }

    GroupCreateContent(
        navController = navController,
        uiState = uiState,
        onIntent = { viewModel.eventIntent(it) },
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GroupCreateContent(
    navController: NavController,
    uiState: GroupCreateUiState,
    onIntent: (GroupCreateIntent) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    NavigationBackIconButton(onClick = {
                        if (uiState.currentStep > 0) {
                            onIntent(GroupCreateIntent.Back)
                        } else {
                            navController.popBackStack()
                        }
                    })
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            if (!uiState.isLoading && uiState.currentStep < 2) {
                ExtendedFloatingActionButton(onClick = {
                    onIntent(GroupCreateIntent.NextStep)
                }) {
                    Icon(Icons.Filled.Check, contentDescription = null)
                    Text("Próximo")
                }
            } else if (!uiState.isLoading) {
                ExtendedFloatingActionButton(onClick = {
                    onIntent(GroupCreateIntent.CreateGroup)
                }) {
                    Icon(Icons.Filled.Check, contentDescription = null)
                    Text(stringResource(R.string.group_create_action_save_group))
                }
            }
        }
    ) { padding ->
        when {
            uiState.isLoading -> LoadingProgress(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
            )

            uiState.isSuccess -> SuccessComponent(
                modifier = Modifier.fillMaxSize(),
                redirectTo = { navController.popBackStack() }
            )

            uiState.errorMessage != null -> ErrorComponent(
                modifier = Modifier.fillMaxSize(),
                message = uiState.errorMessage,
                onBack = { navController.popBackStack() },
                onRefresh = { onIntent(GroupCreateIntent.FetchContacts) }
            )

            else -> LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                when (uiState.currentStep) {
                    0 -> {
                        item { StepOneGroupInfo(uiState, onIntent) }
                    }

                    1 -> {
                        item { StepTwoMembers(uiState, onIntent) { showBottomSheet = true } }
                    }

                    2 -> {
                        item { StepThreeDrawInfo(uiState) }
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        AddMemberBottomSheet(
            onDismiss = { showBottomSheet = false },
            onMemberAdded = { member -> onIntent(GroupCreateIntent.ToggleMember(member)) }
        )
    }
}

@Composable
private fun StepOneGroupInfo(
    uiState: GroupCreateUiState,
    onIntent: (GroupCreateIntent) -> Unit
) {
    Column {
        OutlinedTextField(
            value = uiState.name,
            onValueChange = { onIntent(GroupCreateIntent.UpdateName(it)) },
            label = { Text(stringResource(R.string.group_create_input_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.description,
            onValueChange = { onIntent(GroupCreateIntent.UpdateDescription(it)) },
            label = { Text(stringResource(R.string.group_create_input_description)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.drawDate,
            onValueChange = { onIntent(GroupCreateIntent.UpdateDrawDate(it)) },
            label = { Text("Data do sorteio") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.minValue,
            onValueChange = { onIntent(GroupCreateIntent.UpdateMinValue(it)) },
            label = { Text("Valor mínimo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.maxValue,
            onValueChange = { onIntent(GroupCreateIntent.UpdateMaxValue(it)) },
            label = { Text("Valor máximo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        DrawTypeDropdown(
            selectedType = uiState.drawType,
            onTypeSelected = { onIntent(GroupCreateIntent.UpdateDrawType(it)) }
        )
    }
}

@Composable
private fun StepTwoMembers(
    uiState: GroupCreateUiState,
    onIntent: (GroupCreateIntent) -> Unit,
    showBottomSheet: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { showBottomSheet() }) {
                Text(stringResource(R.string.group_create_text_button_member))
            }
            IconButton(onClick = { showBottomSheet() }) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }

        // Membros selecionados
        uiState.members.forEach { member ->
            ContactItem(
                contact = member,
                isSelected = true,
                action = { _, _ ->
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                onIntent(GroupCreateIntent.ToggleMember(member))
                            }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
            )
        }

        // Contatos disponíveis (não selecionados)
        uiState.contacts.filterNot { it in uiState.members }.forEach { contact ->
            ContactItem(
                contact = contact,
                isSelected = uiState.members.contains(contact),
                action = { _, _ ->
                    Checkbox(
                        checked = uiState.members.contains(contact),
                        onCheckedChange = {
                            onIntent(GroupCreateIntent.ToggleMember(contact))
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun StepThreeDrawInfo(
    uiState: GroupCreateUiState
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text("Confirmação do Grupo", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Nome: ${uiState.name}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Descrição: ${uiState.description}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Data do Sorteio: ${uiState.drawDate}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Valor Mínimo: ${uiState.minValue}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Valor Máximo: ${uiState.maxValue}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Tipo de Sorteio: ${uiState.drawType}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Membros:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.members.isEmpty()) {
            Text("Nenhum membro adicionado", style = MaterialTheme.typography.bodyMedium)
        } else {
            uiState.members.forEach { member ->
                Text("- ${member.name}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


@Composable
fun DrawTypeDropdown(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val types = listOf("Padrão", "Manual", "Outro")

    Column {
        Text("Tipo de sorteio", style = MaterialTheme.typography.labelLarge)
        Box {
            OutlinedTextField(
                value = selectedType,
                onValueChange = {},
                label = { Text("Tipo") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            onTypeSelected(type)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

private class GroupCreatePreviewProvider : PreviewParameterProvider<GroupCreateUiState> {
    override val values = sequenceOf(
        GroupCreateUiState(),
        GroupCreateUiState(isLoading = true),
        GroupCreateUiState(isSuccess = true),
        GroupCreateUiState(errorMessage = "Erro ao salvar")
    )
}

@Composable
@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true)
fun GroupCreateScreenPreview(
    @PreviewParameter(GroupCreatePreviewProvider::class) state: GroupCreateUiState,
) {
    FriendsSecretsTheme {
        GroupCreateContent(
            navController = rememberNavController(),
            uiState = state,
            onIntent = {}
        )
    }
}
