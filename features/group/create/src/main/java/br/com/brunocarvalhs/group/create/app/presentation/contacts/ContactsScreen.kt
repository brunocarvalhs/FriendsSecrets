package br.com.brunocarvalhs.group.create.app.presentation.contacts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.AddManualMemberForm
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.ContactList
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.SearchField
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.SelectedMembersRow
import br.com.brunocarvalhs.group.create.app.presentation.forms.components.LoadingProgress
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter

@Composable
internal fun ContactsScreen(
    viewModel: ContactsViewModel,
    onBack: () -> Unit,
    onNext: (FormsRouter) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    ContactsContent(
        contacts = uiState.contacts,
        selectedMembers = uiState.members,
        filteredContacts = uiState.filteredContacts,
        searchQuery = uiState.searchQuery,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onQueryChange = { viewModel.handleIntent(intent = ContactsIntent.SearchContacts(query = it)) },
        onToggleMember = { viewModel.handleIntent(intent = ContactsIntent.AddMember(contact = it)) },
        onRemoveMember = { viewModel.handleIntent(intent = ContactsIntent.RemoveMember(contact = it)) },
        onRefresh = { viewModel.handleIntent(intent = ContactsIntent.LoadContacts) },
        onBack = onBack,
        onNext = {
            viewModel.handleIntent(
                intent = ContactsIntent.Next(
                    callback = { router -> onNext(router) }
                )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactsContent(
    contacts: List<UserModel>,
    selectedMembers: List<UserModel>,
    filteredContacts: List<UserModel>,
    searchQuery: String,
    isLoading: Boolean,
    error: String?,
    onQueryChange: (String) -> Unit,
    onToggleMember: (UserModel) -> Unit,
    onRemoveMember: (UserModel) -> Unit,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    onNext: (List<UserModel>) -> Unit,
    initialShowManualForm: Boolean = false // Adicionado para facilitar o preview
) {
    var showManualForm by remember { mutableStateOf(initialShowManualForm) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Adicionar membros",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (!isLoading) {
                            Text(
                                text = "${selectedMembers.size}/${contacts.size.coerceAtLeast(selectedMembers.size)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                actions = {
                    if (!isLoading) {
                        TextButton(onClick = { onNext(selectedMembers) }) {
                            Text(
                                text = "Avançar",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        when {
            isLoading -> {
                LoadingProgress(modifier = Modifier.padding(paddingValues))
            }

            contacts.isEmpty() || error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (selectedMembers.isNotEmpty()) {
                        SelectedMembersRow(
                            members = selectedMembers,
                            onRemoveMember = { onRemoveMember(it) }
                        )
                    }

                    AddManualMemberForm(
                        onAddMember = { onToggleMember(it) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onRefresh,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Sincronizar Contatos / Tentar Novamente")
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    SearchField(
                        query = searchQuery,
                        onQueryChange = { onQueryChange(it) }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { showManualForm = !showManualForm }
                        ) {
                            Icon(
                                if (showManualForm) Icons.Default.ArrowBackIosNew else Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(if (showManualForm) "Voltar para contatos" else "Adicionar pessoa manualmente")
                        }
                    }

                    AnimatedVisibility(visible = showManualForm) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            AddManualMemberForm(
                                onAddMember = { 
                                    onToggleMember(it)
                                    showManualForm = false
                                }
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                        }
                    }

                    if (!showManualForm) {
                        if (selectedMembers.isNotEmpty()) {
                            SelectedMembersRow(
                                members = selectedMembers,
                                onRemoveMember = { onRemoveMember(it) }
                            )
                        }

                        ContactList(
                            contacts = filteredContacts,
                            selectedMembers = selectedMembers,
                            onToggleMember = { onToggleMember(it) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Estado Normal")
@Composable
private fun ContactsContentNormalPreview() {
    MaterialTheme {
        Surface {
            ContactsContent(
                contacts = List(10) { UserModel(name = "Contato $it", phoneNumber = "9999999$it") },
                selectedMembers = listOf(UserModel(name = "Selecionado 1", phoneNumber = "11111")),
                filteredContacts = List(10) { UserModel(name = "Contato $it", phoneNumber = "9999999$it") },
                searchQuery = "",
                isLoading = false,
                error = null,
                onQueryChange = {},
                onToggleMember = {},
                onRemoveMember = {},
                onRefresh = {},
                onBack = {},
                onNext = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Estado Carregando")
@Composable
private fun ContactsContentLoadingPreview() {
    MaterialTheme {
        Surface {
            ContactsContent(
                contacts = emptyList(),
                selectedMembers = emptyList(),
                filteredContacts = emptyList(),
                searchQuery = "",
                isLoading = true,
                error = null,
                onQueryChange = {},
                onToggleMember = {},
                onRemoveMember = {},
                onRefresh = {},
                onBack = {},
                onNext = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Sem Contatos / Erro Permissão")
@Composable
private fun ContactsContentEmptyOrErrorPreview() {
    MaterialTheme {
        Surface {
            ContactsContent(
                contacts = emptyList(),
                selectedMembers = emptyList(),
                filteredContacts = emptyList(),
                searchQuery = "",
                isLoading = false,
                error = "Permission Denied",
                onQueryChange = {},
                onToggleMember = {},
                onRemoveMember = {},
                onRefresh = {},
                onBack = {},
                onNext = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Adição Manual (com lista disponível)")
@Composable
private fun ContactsContentManualModePreview() {
    MaterialTheme {
        Surface {
            ContactsContent(
                contacts = List(5) { UserModel(name = "Contato $it", phoneNumber = "99999$it") },
                selectedMembers = emptyList(),
                filteredContacts = List(5) { UserModel(name = "Contato $it", phoneNumber = "99999$it") },
                searchQuery = "",
                isLoading = false,
                error = null,
                onQueryChange = {},
                onToggleMember = {},
                onRemoveMember = {},
                onRefresh = {},
                onBack = {},
                onNext = {},
                initialShowManualForm = true
            )
        }
    }
}
