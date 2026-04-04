package br.com.brunocarvalhs.group.create.app.presentation.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.brunocarvalhs.group.app.presentation.create.GroupCreateIntent
import br.com.brunocarvalhs.group.app.presentation.create.GroupCreateUiState
import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel
import br.com.brunocarvalhs.group.create.app.presentation.forms.components.MemberAvatarItem
import br.com.brunocarvalhs.group.create.app.presentation.forms.components.SettingsItem

@Composable
fun FormsScreen(
    navController: NavController,
    viewModel: FormsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    FormsContent(
        name = uiState.name,
        onNameChange = { viewModel.handleIntent(FormsIntent.UpdateName(it)) },
        members = uiState.members,
        contacts = uiState.contacts,
        onBack = {
            navController.popBackStack()
        },
        onCreate = { viewModel.handleIntent(FormsIntent.CreateGroup) },
        onToggleMember = { viewModel.handleIntent(FormsIntent.ToggleMember(it)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormsContent(
    name: String,
    onNameChange: (String) -> Unit,
    members: List<ContactModel>,
    contacts: List<ContactModel>,
    onCreate: () -> Unit,
    onBack: () -> Unit,
    onToggleMember: (ContactModel) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Novo grupo",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color(0xFF00A884)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { onCreate() }) {
                        Text(
                            text = "Criar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0B141B),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF0B141B)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = Color(0xFF1F2C34),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2C3E46))
                            .clickable { /* Ação de foto */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color(0xFF8696A0),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color(0xFF8696A0),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    TextField(
                        value = name,
                        onValueChange = { onNameChange(it) },
                        placeholder = {
                            Text(
                                "Nome do grupo (opcional)",
                                color = Color(0xFF8696A0),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color(0xFF00A884),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }

            Surface(
                color = Color(0xFF1F2C34),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    SettingsItem(
                        title = "Mensagens temporárias",
                        value = "Desativadas",
                        onClick = {}
                    )
                    HorizontalDivider(
                        color = Color(0xFF233138),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    SettingsItem(
                        title = "Permissões do grupo",
                        onClick = {}
                    )
                }
            }

            Column {
                Text(
                    text = "MEMBROS: ${members.size} DE ${contacts.size}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = Color(0xFF8696A0),
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(members) { member ->
                        MemberAvatarItem(member = member) {
                            onToggleMember(member)
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun FormsContentPreview() {
    FormsContent(
        contacts = listOf(
            ContactModel(id = "1", name = "Alice", phoneNumber = "1234567890"),
            ContactModel(id = "2", name = "Bob", phoneNumber = "0987654321")
        ),
        members = listOf(
            ContactModel(id = "1", name = "Alice", phoneNumber = "1234567890"),
            ContactModel(id = "2", name = "Bob", phoneNumber = "0987654321")
        ),
        name = "Meu Grupo",
        onNameChange = {},
        onCreate = {},
        onBack = {},
        onToggleMember = {}
    )
}
