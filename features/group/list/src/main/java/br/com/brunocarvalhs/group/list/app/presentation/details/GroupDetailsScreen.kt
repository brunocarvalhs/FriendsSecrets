package br.com.brunocarvalhs.group.list.app.presentation.details

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.list.app.domain.entities.UserModel
import br.com.brunocarvalhs.group.list.app.presentation.details.components.ActionIconCard
import br.com.brunocarvalhs.group.list.app.presentation.details.components.MemberItem
import br.com.brunocarvalhs.group.list.app.presentation.details.components.SectionHeader
import br.com.brunocarvalhs.group.list.app.presentation.details.components.SettingItem
import coil.compose.AsyncImage

@Composable
fun GroupDetailsScreen(
    viewModel: GroupDetailsViewModel,
    onBack: () -> Unit = {},
    onChat: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    GroupDetailsContent(
        uiState = uiState,
        onBack = onBack,
        onDraw = { viewModel.handleIntent(GroupDetailsIntent.Draw) },
        onReveal = { viewModel.handleIntent(GroupDetailsIntent.Reveal) },
        onChat = onChat,
        onDelete = { viewModel.handleIntent(GroupDetailsIntent.Delete(onBack)) },
        onShareGroup = { viewModel.handleIntent(GroupDetailsIntent.Share) },
        onExit = { viewModel.handleIntent(GroupDetailsIntent.Exit(onBack)) },
        onSelectMember = { viewModel.handleIntent(GroupDetailsIntent.SelectMember(it)) },
        onCodeChange = { viewModel.handleIntent(GroupDetailsIntent.ChangeCode(it)) },
        onConfirmReveal = { viewModel.handleIntent(GroupDetailsIntent.ConfirmReveal(it)) },
        onDismissReveal = { viewModel.handleIntent(GroupDetailsIntent.DismissReveal) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupDetailsContent(
    uiState: GroupDetailsUiState,
    onBack: () -> Unit,
    onDraw: () -> Unit,
    onReveal: () -> Unit,
    onChat: () -> Unit,
    onDelete: () -> Unit,
    onExit: () -> Unit,
    onShareGroup: () -> Unit,
    onSelectMember: (String) -> Unit,
    onCodeChange: (String) -> Unit,
    onConfirmReveal: (String) -> Unit,
    onDismissReveal: () -> Unit,
) {
    val group = uiState.group
    var showMenu by remember { mutableStateOf(false) }
    val isDrawn = group.draws.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Mais opções")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            if (group.isOwner) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Excluir grupo",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    },
                                    onClick = {
                                        showMenu = false
                                        onDelete()
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                )
                            } else {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Sair do grupo",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    },
                                    onClick = {
                                        showMenu = false
                                        onExit()
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ExitToApp,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    model = group.photo,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Grupo · ${group.members.size} participantes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionIconCard(Icons.Default.Share, "Convidar", onShareGroup)
                    ActionIconCard(Icons.AutoMirrored.Filled.Chat, "Chat", onChat)
                    ActionIconCard(
                        icon = Icons.Default.Casino,
                        label = if (isDrawn) "Revelar" else "Sortear",
                        onClick = if (isDrawn) onReveal else onDraw
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = group.description.ifBlank { "Adicionar descrição ao grupo" },
                        color = if (group.description.isBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Criado em ${group.createdAt ?: "Desconhecido"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
            }

            item {
                SectionHeader(title = "Detalhes do sorteio")
                group.createdAt?.let {
                    SettingItem(
                        Icons.Default.CalendarToday,
                        "Data do sorteio",
                        it
                    )
                }
                if (group.minPrice != null || group.maxPrice != null) {
                    val priceRange = if (group.minPrice != null && group.maxPrice != null) {
                        "Entre R$ ${group.minPrice} e R$ ${group.maxPrice}"
                    } else if (group.minPrice != null) {
                        "A partir de R$ ${group.minPrice}"
                    } else {
                        "Até R$ ${group.maxPrice}"
                    }
                    SettingItem(Icons.Default.AttachMoney, "Faixa de preço", priceRange)
                }
                group.type?.let { SettingItem(Icons.Default.CardGiftcard, "Tipo de presente", it) }
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
            }

            item {
                SectionHeader(
                    title = "${group.members.size} participantes",
                )
                if (group.isOwner && group.draws.isEmpty()) {
                    SettingItem(
                        Icons.Default.PersonAdd,
                        "Adicionar participantes",
                        iconColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary
                    )
                }
            }

            items(group.members) { member ->
                MemberItem(
                    participant = member.name,
                    draws = group.draws,
                    isAdministrator = group.isOwner,
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
                if (group.isOwner) {
                    SettingItem(Icons.Default.Edit, "Editar informações do grupo")
                }
                SettingItem(
                    icon = if (group.isOwner) Icons.Default.Delete else Icons.AutoMirrored.Filled.ExitToApp,
                    title = if (group.isOwner) "Apagar grupo" else "Sair do grupo",
                    textColor = MaterialTheme.colorScheme.error,
                    iconColor = MaterialTheme.colorScheme.error,
                    onClick = if (group.isOwner) onDelete else onExit
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (uiState.showRevealModal) {
        RevealDialog(
            uiState = uiState,
            onDismiss = onDismissReveal,
            onSelectMember = onSelectMember,
            onCodeChange = onCodeChange,
            onConfirm = onConfirmReveal
        )
    }
}

@Composable
fun RevealDialog(
    uiState: GroupDetailsUiState,
    onDismiss: () -> Unit,
    onSelectMember: (String) -> Unit,
    onCodeChange: (String) -> Unit,
    onConfirm: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (uiState.revealedFriend != null) "Seu Amigo Secreto" else "Revelar Amigo Secreto") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (uiState.revealedFriend != null) {
                    Text(
                        text = "Seu amigo secreto é:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.revealedFriend,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (uiState.selectedMember == null) {
                    Text(text = "Selecione quem você é no grupo:")
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(modifier = Modifier.height(200.dp)) {
                        items(uiState.group.members) { member ->
                            Text(
                                text = member.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectMember(member.name) }
                                    .padding(vertical = 12.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            HorizontalDivider(thickness = 0.5.dp)
                        }
                    }
                } else {
                    Text(text = "Olá ${uiState.selectedMember}, digite o código do grupo para revelar:")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = uiState.revelationCode,
                        onValueChange = onCodeChange,
                        label = { Text("Código do Grupo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            if (uiState.revealedFriend != null) {
                Button(onClick = onDismiss) {
                    Text("Fechar")
                }
            } else if (uiState.selectedMember != null) {
                Button(
                    onClick = { onConfirm(uiState.selectedMember) },
                    enabled = uiState.revelationCode == uiState.group.token
                ) {
                    Text("Revelar")
                }
            }
        },
        dismissButton = {
            if (uiState.revealedFriend == null) {
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun GroupDetailsPreview() {
    GroupDetailsContent(
        uiState = GroupDetailsUiState(
            group = GroupModel(
                id = "1",
                name = "Amigo Secreto da Família",
                description = "Troca de presentes de Natal 2024. Vamos fazer algo bem legal este ano!",
                members = listOf(
                    UserModel("Bruno", listOf("Tecnologia", "Livros")),
                    UserModel("João", listOf("Esportes")),
                    UserModel("Maria", listOf("Culinária")),
                    UserModel("Ana", listOf("Música")),
                ),
                isOwner = true,
                createdAt = "20/10/2024",
                type = "Qualquer coisa",
                minPrice = 50,
                maxPrice = 200
            )
        ),
        onBack = {},
        onDraw = {},
        onReveal = {},
        onChat = {},
        onDelete = {},
        onExit = {},
        onShareGroup = {},
        onSelectMember = {},
        onCodeChange = {},
        onConfirmReveal = {},
        onDismissReveal = {}
    )
}

@Preview(showBackground = true, name = "Sorteio Realizado")
@Composable
private fun GroupDetailsDrawnPreview() {
    GroupDetailsContent(
        uiState = GroupDetailsUiState(
            group = GroupModel(
                id = "1",
                name = "Amigo Secreto da Família",
                description = "Troca de presentes de Natal 2024",
                members = listOf(
                    UserModel("Bruno", listOf("Tecnologia")),
                    UserModel("João", listOf("Esportes")),
                ),
                draws = mapOf("Bruno" to "João"),
                isOwner = false,
                createdAt = "20/10/2024",
                type = "Qualquer coisa",
                minPrice = 50,
                maxPrice = 200
            )
        ),
        onBack = {},
        onDraw = {},
        onReveal = {},
        onChat = {},
        onDelete = {},
        onExit = {},
        onShareGroup = {},
        onSelectMember = {},
        onCodeChange = {},
        onConfirmReveal = {},
        onDismissReveal = {}
    )
}
