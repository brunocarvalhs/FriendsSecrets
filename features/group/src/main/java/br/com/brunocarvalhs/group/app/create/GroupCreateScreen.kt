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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import br.com.brunocarvalhs.group.commons.ui.components.DateInputField
import br.com.brunocarvalhs.group.commons.ui.components.DrawTypeDropdown
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
            if (!uiState.isLoading) {
                ExtendedFloatingActionButton(onClick = {
                    if (uiState.currentStep < 2) {
                        onIntent(GroupCreateIntent.NextStep)
                    } else {
                        onIntent(GroupCreateIntent.CreateGroup)
                    }
                }) {
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
                        item {
                            StepThreeDrawInfo(
                                uiState,
                                onEdit = { step -> onIntent(GroupCreateIntent.GoToStep(step)) })
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
private fun StepOneGroupInfo(
    uiState: GroupCreateUiState,
    onIntent: (GroupCreateIntent) -> Unit
) {
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
private fun StepTwoMembers(
    uiState: GroupCreateUiState,
    onIntent: (GroupCreateIntent) -> Unit,
    showBottomSheet: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredContacts = uiState.contacts.filter { contact ->
        contact !in uiState.members && contact.name.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.group_create_members_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(
                onClick = showBottomSheet,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.adicionar_membro),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(stringResource(R.string.search_contacts)) },
            placeholder = { Text(stringResource(R.string.search_contacts_placeholder)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.group_create_members_added),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.members.isEmpty()) {
            Text(
                text = stringResource(R.string.nenhum_membro_adicionado_ainda),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uiState.members) { member ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(80.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = member.name.take(1).uppercase(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(
                                onClick = { onIntent(GroupCreateIntent.ToggleMember(member)) },
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.TopEnd)
                                    .background(
                                        color = MaterialTheme.colorScheme.errorContainer,
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Remover ${member.name}",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = member.name,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.group_create_contacts_available),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredContacts.isEmpty()) {
            Text(
                text = stringResource(R.string.no_contacts_found),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(vertical = 12.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredContacts) { contact ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ContactItem(
                            contact = contact,
                            isSelected = false,
                            action = { _, _ ->
                                onIntent(GroupCreateIntent.ToggleMember(contact))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepThreeDrawInfo(
    uiState: GroupCreateUiState,
    onEdit: (step: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = stringResource(R.string.confirma_o_do_grupo),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Surface(
            tonalElevation = 4.dp,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.informa_es_do_grupo),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                // Informações principais
                GroupInfoRow(label = stringResource(R.string.nome), value = uiState.name)
                GroupInfoRow(label = stringResource(R.string.descri_o), value = uiState.description)
                GroupInfoRow(label = stringResource(R.string.data_do_sorteio), value = uiState.drawDate)
                GroupInfoRow(label = stringResource(R.string.info_valor_m_nimo), value = uiState.minValue)
                GroupInfoRow(label = stringResource(R.string.info_valor_m_ximo), value = uiState.maxValue)
                GroupInfoRow(label = stringResource(R.string.info_tipo_de_sorteio), value = uiState.drawType)

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { onEdit(0) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(R.string.editar))
                }
            }
        }

        Surface(
            tonalElevation = 4.dp,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.membros),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                if (uiState.members.isEmpty()) {
                    Text(
                        text = stringResource(R.string.nenhum_membro_adicionado),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        uiState.members.forEach { member ->
                            Text(
                                text = "- ${member.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { onEdit(1) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(R.string.editar_membros))
                }
            }
        }
    }
}

@Composable
private fun GroupInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
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
