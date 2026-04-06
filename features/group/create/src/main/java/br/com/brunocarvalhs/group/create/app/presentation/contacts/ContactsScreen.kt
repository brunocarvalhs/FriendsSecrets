package br.com.brunocarvalhs.group.create.app.presentation.contacts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.ContactList
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.SearchField
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.SelectedMembersRow
import br.com.brunocarvalhs.group.create.app.presentation.forms.components.ErrorComponent
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
    contacts: List<ContactModel>,
    selectedMembers: List<ContactModel>,
    filteredContacts: List<ContactModel>,
    searchQuery: String,
    isLoading: Boolean,
    error: String?,
    onQueryChange: (String) -> Unit,
    onToggleMember: (ContactModel) -> Unit,
    onRemoveMember: (ContactModel) -> Unit,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    onNext: (List<ContactModel>) -> Unit
) {
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
                        if (!isLoading && error == null) {
                            Text(
                                text = "${selectedMembers.size}/${contacts.size}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (error == null) onBack()
                        else onRefresh()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                actions = {
                    if (!isLoading && error == null) {
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

            error != null -> {
                ErrorComponent(
                    modifier = Modifier.padding(paddingValues),
                    message = error,
                    onRefresh = onRefresh,
                    onBack = onBack
                )
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

@Preview
@Composable
private fun ContactsContentPreview() {
    ContactsContent(
        contacts = listOf(
            ContactModel(name = "John Doe", phoneNumber = "1234567890"),
            ContactModel(name = "Jane Smith", phoneNumber = "9876543210")
        ),
        selectedMembers = listOf(
            ContactModel(name = "John Doe", phoneNumber = "1234567890")
        ),
        filteredContacts = listOf(
            ContactModel(name = "John Doe", phoneNumber = "1234567890"),
            ContactModel(name = "Jane Smith", phoneNumber = "9876543210")
        ),
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
