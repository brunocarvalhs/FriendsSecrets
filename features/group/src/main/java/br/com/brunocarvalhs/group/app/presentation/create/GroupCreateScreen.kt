package br.com.brunocarvalhs.group.app.presentation.create

import android.Manifest
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.brunocarvalhs.friendssecrets.ui.components.ContactItem
import br.com.brunocarvalhs.friendssecrets.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.ui.components.SuccessComponent
import br.com.brunocarvalhs.friendssecrets.ui.fake.toFake
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.group.R
import br.com.brunocarvalhs.group.commons.ui.components.DateInputField
import br.com.brunocarvalhs.group.commons.ui.components.DrawTypeDropdown
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GroupCreateScreen(
    navController: NavController,
    viewModel: GroupCreateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val contactPermissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)

    LaunchedEffect(Unit) {
        if (!contactPermissionState.status.isGranted) {
            contactPermissionState.launchPermissionRequest()
        } else {
            viewModel.eventIntent(GroupCreateIntent.FetchContacts)
        }
    }

    when {
        uiState.isSuccess -> SuccessComponent(
            modifier = Modifier.fillMaxSize(),
            redirectTo = { navController.popBackStack() }
        )

        else -> {
            GroupCreateContent(
                uiState = uiState,
                onIntent = viewModel::eventIntent,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupCreateContent(
    uiState: GroupCreateUiState,
    onIntent: (GroupCreateIntent) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.group_create_title)) },
                navigationIcon = { NavigationBackIconButton(onClick = onBack) }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onIntent(GroupCreateIntent.CreateGroup) },
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.group_create_action_save_group))
            }
        }
    ) { padding ->
        when {
            uiState.errorMessage != null -> ErrorComponent(
                modifier = Modifier.fillMaxSize(),
                message = uiState.errorMessage,
                onBack = onBack,
                onRefresh = { onIntent(GroupCreateIntent.ClearError) }
            )

            else -> LazyColumn(
                modifier = Modifier
                    .padding(paddingValues = padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 96.dp
                ),
            ) {
                item { GroupInfoSection(uiState, onIntent) }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item { GroupConfigSection(uiState, onIntent) }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                val listToShow: List<UserEntities> =
                    uiState.filteredContacts.ifEmpty { uiState.contacts }

                item {
                    Text(
                        stringResource(R.string.group_create_members_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    uiState.fieldErrors[GroupEntities.MEMBERS]?.let { error ->
                        Text(
                            text = stringResource(error),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { onIntent(GroupCreateIntent.UpdateSearch(it)) },
                        label = { Text(text = stringResource(R.string.search_contacts)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                    )
                }

                when {
                    uiState.isLoading -> {
                        item {
                            Box(Modifier.fillMaxWidth()) {
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }
                        }
                    }

                    listToShow.isEmpty() -> {
                        item { Text(text = stringResource(R.string.no_contacts_found)) }
                    }

                    else -> {
                        items(items = listToShow) { contact ->
                            ContactItem(
                                contact = contact,
                                paddingValues = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                                isSelected = uiState.members.contains(contact),
                                action = { user, _ ->
                                    Checkbox(
                                        checked = uiState.members.contains(user),
                                        onCheckedChange = {
                                            onIntent(
                                                GroupCreateIntent.ToggleMember(
                                                    user
                                                )
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupInfoSection(
    uiState: GroupCreateUiState,
    onIntent: (GroupCreateIntent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            stringResource(R.string.group_info_title),
            style = MaterialTheme.typography.titleMedium
        )

        // Nome do grupo
        OutlinedTextField(
            value = uiState.name,
            onValueChange = { onIntent(GroupCreateIntent.UpdateName(it)) },
            label = { Text(stringResource(R.string.group_create_input_name)) },
            singleLine = true,
            isError = uiState.fieldErrors[GroupEntities.NAME] != null,
            modifier = Modifier.fillMaxWidth()
        )
        uiState.fieldErrors[GroupEntities.NAME]?.let { error ->
            Text(
                text = stringResource(error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        OutlinedTextField(
            value = uiState.description,
            onValueChange = { onIntent(GroupCreateIntent.UpdateDescription(it)) },
            label = { Text(stringResource(R.string.group_create_input_description)) },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.fieldErrors[GroupEntities.DESCRIPTION] != null
        )
        uiState.fieldErrors[GroupEntities.DESCRIPTION]?.let { error ->
            Text(
                text = stringResource(error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        DateInputField(
            value = uiState.drawDate,
            onValueChange = { onIntent(GroupCreateIntent.UpdateDrawDate(it)) }
        )
        uiState.fieldErrors[GroupEntities.DATE]?.let { error ->
            Text(
                text = stringResource(error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun GroupConfigSection(
    uiState: GroupCreateUiState,
    onIntent: (GroupCreateIntent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            stringResource(R.string.group_config_title),
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = uiState.minValue,
                    onValueChange = { onIntent(GroupCreateIntent.UpdateMinValue(it)) },
                    label = { Text(stringResource(R.string.valor_m_nimo)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = uiState.fieldErrors[GroupEntities.MIN_PRICE] != null
                )
                uiState.fieldErrors[GroupEntities.MIN_PRICE]?.let { error ->
                    Text(
                        text = stringResource(error),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = uiState.maxValue,
                    onValueChange = { onIntent(GroupCreateIntent.UpdateMaxValue(it)) },
                    label = { Text(stringResource(R.string.valor_m_ximo)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = uiState.fieldErrors[GroupEntities.MAX_PRICE] != null
                )
                uiState.fieldErrors[GroupEntities.MAX_PRICE]?.let { error ->
                    Text(
                        text = stringResource(error),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }
        }

        Column {
            DrawTypeDropdown(
                selectedType = uiState.drawType,
                onTypeSelected = { onIntent(GroupCreateIntent.UpdateDrawType(it)) }
            )
            uiState.fieldErrors[GroupEntities.TYPE]?.let { error ->
                Text(
                    text = stringResource(error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    }
}

private class GroupCreatePreviewProvider : PreviewParameterProvider<GroupCreateUiState> {
    override val values = sequenceOf(
        // 1. Estado inicial vazio
        GroupCreateUiState(),

        // 2. Preenchendo dados do grupo
        GroupCreateUiState(
            name = "Amigos da Faculdade",
            description = "Grupo do Amigo Secreto 2025",
            drawDate = "25/12/2025",
            minValue = "20",
            maxValue = "200",
            drawType = "Tradicional"
        ),

        // 3. Membros selecionados + contatos carregados
        GroupCreateUiState(
            name = "Família Silva",
            description = "Natal em família",
            drawDate = "24/12/2025",
            minValue = "50",
            maxValue = "150",
            drawType = "Online",
            contacts = listOf(
                UserEntities.toFake(id = "1", name = "Alice Souza", photoUrl = null),
                UserEntities.toFake(id = "2", name = "Bruno Lima", photoUrl = null),
                UserEntities.toFake(id = "3", name = "Carla Fernandes", photoUrl = null)
            ),
            filteredContacts = listOf(
                UserEntities.toFake(id = "1", name = "Alice Souza", photoUrl = null),
                UserEntities.toFake(id = "2", name = "Bruno Lima", photoUrl = null),
                UserEntities.toFake(id = "3", name = "Carla Fernandes", photoUrl = null)
            ),
            members = listOf(
                UserEntities.toFake(id = "1", name = "Alice Souza", photoUrl = null),
                UserEntities.toFake(id = "3", name = "Carla Fernandes", photoUrl = null)
            ),
            isValid = true
        ),

        // 4. Carregando contatos
        GroupCreateUiState(isLoading = true),

        // 5. Erro de validação de campos
        GroupCreateUiState(
            name = "",
            minValue = "200",
            maxValue = "100",
            fieldErrors = mapOf(
                GroupEntities.NAME to R.string.error_name_required,
                GroupEntities.MIN_PRICE to R.string.error_min_value_required,
                GroupEntities.MAX_PRICE to R.string.error_max_value_required,
            )
        ),

        // 6. Sucesso
        GroupCreateUiState(isSuccess = true)
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
            uiState = state,
            onIntent = {},
            onBack = {}
        )
    }
}
