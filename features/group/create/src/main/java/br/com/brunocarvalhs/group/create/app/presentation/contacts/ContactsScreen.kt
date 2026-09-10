package br.com.brunocarvalhs.group.create.app.presentation.contacts

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.group.create.R
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.AddManualMemberForm
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.ContactList
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.SearchField
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.SelectedMembersRow
import br.com.brunocarvalhs.group.create.app.presentation.forms.components.LoadingProgress
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

private const val MIN_MEMBERS = 3
private const val PACKAGE = "package"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun ContactsScreen(
    viewModel: ContactsViewModel,
    onBack: () -> Unit,
    onNext: (Any) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val contactsPermissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)

    LaunchedEffect(Unit) {
        if (!contactsPermissionState.status.isGranted && !contactsPermissionState.status.shouldShowRationale) {
            contactsPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(contactsPermissionState.status.isGranted) {
        if (contactsPermissionState.status.isGranted) {
            viewModel.handleIntent(ContactsIntent.LoadContacts)
        }
    }

    ContactsContent(
        selectedMembers = uiState.members,
        filteredContacts = uiState.filteredContacts,
        searchQuery = uiState.searchQuery,
        isLoading = uiState.isLoading,
        isPermissionGranted = contactsPermissionState.status.isGranted,
        shouldShowRationale = contactsPermissionState.status.shouldShowRationale,
        onRequestPermission = { contactsPermissionState.launchPermissionRequest() },
        onOpenSettings = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts(PACKAGE, context.packageName, null)
            }
            context.startActivity(intent)
        },
        onQueryChange = { viewModel.handleIntent(intent = ContactsIntent.SearchContacts(query = it)) },
        onToggleMember = { viewModel.handleIntent(intent = ContactsIntent.ToggleMember(contact = it)) },
        onRemoveMember = { viewModel.handleIntent(intent = ContactsIntent.RemoveMember(contact = it)) },
        onBack = onBack,
        onNext = {
            viewModel.handleIntent(
                intent = ContactsIntent.Next(
                    callback = { router -> onNext(router) }
                )
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactsContent(
    selectedMembers: List<UserModel>,
    filteredContacts: List<UserModel>,
    searchQuery: String,
    isLoading: Boolean,
    isPermissionGranted: Boolean,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    onQueryChange: (String) -> Unit,
    onToggleMember: (UserModel) -> Unit,
    onRemoveMember: (UserModel) -> Unit,
    onBack: () -> Unit,
    onNext: (List<UserModel>) -> Unit,
) {
    var showManualForm by remember(isPermissionGranted) { mutableStateOf(!isPermissionGranted) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.contacts_adicional_members),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = stringResource(
                                    R.string.contacts_adicional_members_subtitle_select,
                                    selectedMembers.size
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.content_description_back),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = { onNext(selectedMembers) },
                            enabled = !isLoading && selectedMembers.size >= MIN_MEMBERS
                        ) {
                            Text(
                                text = stringResource(R.string.content_description_next),
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                    alpha = if (selectedMembers.size >= MIN_MEMBERS) 1f else 0.5f
                                ),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
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
                if (isPermissionGranted && !showManualForm) {
                    SearchField(
                        query = searchQuery,
                        onQueryChange = onQueryChange
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isPermissionGranted) {
                        TextButton(onClick = { showManualForm = !showManualForm }) {
                            Icon(
                                imageVector = if (showManualForm) Icons.Default.Contacts else Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (showManualForm) {
                                    stringResource(R.string.view_contact_list)
                                } else {
                                    stringResource(R.string.add_manually)
                                }
                            )
                        }
                    } else {
                        val action =
                            if (shouldShowRationale) onRequestPermission else onOpenSettings
                        val label =
                            if (shouldShowRationale) {
                                stringResource(R.string.enable_contact_access)
                            } else {
                                stringResource(R.string.allow_contacts_in_settings)
                            }
                        val icon =
                            if (shouldShowRationale) Icons.Default.Contacts else Icons.Default.Settings

                        TextButton(onClick = action) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(text = label)
                        }
                    }
                }

                if (selectedMembers.isNotEmpty()) {
                    SelectedMembersRow(
                        members = selectedMembers,
                        onRemoveMember = onRemoveMember
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                if (showManualForm) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        AddManualMemberForm(
                            onAddMember = { onToggleMember(it) }
                        )
                    }
                } else if (isPermissionGranted) {
                    ContactList(
                        contacts = filteredContacts,
                        selectedMembers = selectedMembers,
                        onToggleMember = onToggleMember
                    )
                }
            }
        }

        if (isLoading) {
            LoadingProgress()
        }
    }
}
