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
import androidx.navigation.NavController
import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.ContactList
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.SearchField
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.SelectedMembersRow
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    navController: NavController,
    viewModel: ContactsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    ContactsContent(
        contacts = uiState.contacts,
        selectedMembers = uiState.members,
        filteredContacts = uiState.filteredContacts,
        searchQuery = uiState.searchQuery,
        onQueryChange = { viewModel.handleIntent(ContactsIntent.SearchContacts(it)) },
        onToggleMember = { viewModel.handleIntent(ContactsIntent.AddMember(it)) },
        onRemoveMember = { viewModel.handleIntent(ContactsIntent.RemoveMember(it)) },
        onBack = { navController.popBackStack() },
        onNext = { navController.navigate(route = FormsRouter(members = it)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactsContent(
    contacts: List<ContactModel>,
    selectedMembers: List<ContactModel>,
    filteredContacts: List<ContactModel>,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onToggleMember: (ContactModel) -> Unit,
    onRemoveMember: (ContactModel) -> Unit,
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
                        Text(
                            text = "${selectedMembers.size}/${contacts.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
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
                    TextButton(onClick = { onNext(selectedMembers) }) {
                        Text(
                            text = "Avançar",
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
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

@Preview
@Composable
private fun ContactsContentPreview() {
    ContactsContent(
        contacts = listOf(
            ContactModel(
                id = "1",
                name = "John Doe",
                phoneNumber = "1234567890"
            ),
            ContactModel(
                id = "2",
                name = "Jane Smith",
                phoneNumber = "9876543210"
            )
        ),
        selectedMembers = listOf(
            ContactModel(
                id = "1",
                name = "John Doe",
                phoneNumber = "1234567890"
            )
        ),
        filteredContacts = listOf(
            ContactModel(
                id = "1",
                name = "John Doe",
                phoneNumber = "1234567890"
            ),
            ContactModel(
                id = "2",
                name = "Jane Smith",
                phoneNumber = "9876543210"
            )
        ),
        searchQuery = "",
        onQueryChange = {},
        onToggleMember = {},
        onRemoveMember = {},
        onBack = {},
        onNext = {}
    )
}
