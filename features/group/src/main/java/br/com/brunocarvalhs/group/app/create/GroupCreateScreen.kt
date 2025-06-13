package br.com.brunocarvalhs.group.app.create

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.ui.R.string
import br.com.brunocarvalhs.friendssecrets.ui.components.ContactItem
import br.com.brunocarvalhs.friendssecrets.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.ui.components.SuccessComponent
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.group.R
import br.com.brunocarvalhs.group.commons.ui.components.AddMemberBottomSheet
import br.com.brunocarvalhs.group.commons.ui.components.DateInputField
import br.com.brunocarvalhs.group.commons.ui.components.DrawTypeDropdown
import coil.compose.AsyncImage
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
        isStepValid = { viewModel.isStepValid(it) },
        onIntent = { viewModel.eventIntent(it) },
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GroupCreateContent(
    navController: NavController,
    uiState: GroupCreateUiState,
    isStepValid: (GroupCreateUiState) -> Boolean = { true },
    onIntent: (GroupCreateIntent) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.group_create_title)) },
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
            val isEnabled = isStepValid(uiState)

            ExtendedFloatingActionButton(
                onClick = {
                    if (isEnabled) {
                        if (uiState.currentStep < 2) {
                            onIntent(GroupCreateIntent.NextStep)
                        } else {
                            onIntent(GroupCreateIntent.CreateGroup)
                        }
                    }
                },
                containerColor = if (isEnabled)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                contentColor = if (isEnabled)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            ) {
                Icon(
                    imageVector = if (uiState.currentStep < 2) Icons.AutoMirrored.Filled.ArrowForward else Icons.Filled.Check,
                    contentDescription = null
                )
                Text(
                    text = when (uiState.currentStep) {
                        0 -> stringResource(R.string.group_create_next_members)
                        1 -> stringResource(R.string.group_create_next_draw)
                        else -> stringResource(R.string.group_create_action_save_group)
                    }
                )
            }
        }
    ) { padding ->
        when {
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
                        item { StepGroupInfo(uiState, onIntent) }
                    }

                    1 -> {
                        item { StepMembers(uiState, onIntent) { showBottomSheet = true } }
                    }

                    2 -> {
                        item {
                            StepDrawInfo(
                                uiState,
                                onEdit = { step -> onIntent(GroupCreateIntent.GoToStep(step)) }
                            )
                        }
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
private fun StepGroupInfo(
    uiState: GroupCreateUiState,
    onIntent: (GroupCreateIntent) -> Unit
) {
    val isMaxLessThanMin = uiState.minValue.toIntOrNull()?.let { min ->
        uiState.maxValue.toIntOrNull()?.let { max ->
            max < min
        }
    } ?: false

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = uiState.name,
            onValueChange = { onIntent(GroupCreateIntent.UpdateName(it)) },
            label = { Text(stringResource(R.string.group_create_input_name)) },
            placeholder = { Text(stringResource(R.string.ex_amigo_secreto_2025)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = uiState.description,
            onValueChange = { onIntent(GroupCreateIntent.UpdateDescription(it)) },
            label = { Text(stringResource(R.string.group_create_input_description)) },
            placeholder = { Text(stringResource(R.string.descreva_o_prop_sito_do_grupo)) },
            modifier = Modifier.fillMaxWidth()
        )

        DateInputField(
            value = uiState.drawDate,
            onValueChange = { onIntent(GroupCreateIntent.UpdateDrawDate(it)) }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = uiState.minValue,
                onValueChange = {
                    if (it.all { char -> char.isDigit() } && it.length <= 8)
                        onIntent(GroupCreateIntent.UpdateMinValue(it))
                },
                label = { Text(stringResource(R.string.valor_m_nimo)) },
                placeholder = { Text(stringResource(R.string.ex_10)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            OutlinedTextField(
                value = uiState.maxValue,
                onValueChange = {
                    if (it.all { char -> char.isDigit() } && it.length <= 8)
                        onIntent(GroupCreateIntent.UpdateMaxValue(it))
                },
                isError = isMaxLessThanMin,
                supportingText = {
                    if (isMaxLessThanMin) Text("O valor máximo não pode ser menor que o mínimo.")
                },
                label = { Text(stringResource(R.string.valor_m_ximo)) },
                placeholder = { Text(stringResource(R.string.ex_100)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }

        DrawTypeDropdown(
            selectedType = uiState.drawType,
            onTypeSelected = { onIntent(GroupCreateIntent.UpdateDrawType(it)) }
        )
    }
}

@Composable
private fun StepMembers(
    uiState: GroupCreateUiState,
    onIntent: (GroupCreateIntent) -> Unit,
    showBottomSheet: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredContacts = uiState.contacts.filter { contact ->
        contact !in uiState.members && contact.name.contains(searchQuery, ignoreCase = true)
    }

    val scrollState = rememberScrollState()

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.group_create_members_title),
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { showBottomSheet() }) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.adicionar_membro)
                )
            }
        }



        if (uiState.members.isEmpty()) {
            Text(
                stringResource(R.string.nenhum_membro_adicionado_ainda),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.members.forEach { member ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .size(80.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            if (!member.photoUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = member.photoUrl,
                                    contentDescription = stringResource(
                                        string.contact_photo_description,
                                        member.name
                                    ),
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray.copy(alpha = 0.1f)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = member.name.take(1).uppercase(),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            IconButton(
                                onClick = { onIntent(GroupCreateIntent.ToggleMember(member)) },
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Remover ${member.name}",
                                    tint = Color.Red
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = member.name,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(stringResource(R.string.search_contacts)) },
            placeholder = { Text(stringResource(R.string.search_contacts_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            stringResource(R.string.group_create_contacts_available),
            style = MaterialTheme.typography.titleMedium
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            if (filteredContacts.isEmpty()) {
                Text(
                    stringResource(R.string.no_contacts_found),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                filteredContacts.forEach { contact ->
                    ContactItem(
                        contact = contact,
                        isSelected = uiState.members.contains(contact),
                        action = { user, state ->
                            Checkbox(
                                checked = state,
                                onCheckedChange = {
                                    onIntent(GroupCreateIntent.ToggleMember(user))
                                }
                            )
                        }
                    )
                }
            }
        }

    }
}

@Composable
private fun StepDrawInfo(
    uiState: GroupCreateUiState,
    onEdit: (step: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            stringResource(R.string.confirma_o_do_grupo),
            style = MaterialTheme.typography.titleLarge
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.informa_es_do_grupo),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(R.string.nome, uiState.name),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.descri_o, uiState.description),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.data_do_sorteio, uiState.drawDate),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.info_valor_m_nimo, uiState.minValue),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.info_valor_m_ximo, uiState.maxValue),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.info_tipo_de_sorteio, uiState.drawType),
                style = MaterialTheme.typography.bodyLarge
            )
            TextButton(onClick = { onEdit(0) }) { Text(stringResource(R.string.editar)) }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.membros),
                style = MaterialTheme.typography.titleMedium
            )
            if (uiState.members.isEmpty()) {
                Text(
                    text = stringResource(R.string.nenhum_membro_adicionado),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                uiState.members.forEach { member ->
                    Text(text = "- ${member.name}", style = MaterialTheme.typography.bodyMedium)
                }
            }
            TextButton(onClick = { onEdit(1) }) { Text(stringResource(R.string.editar_membros)) }
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
