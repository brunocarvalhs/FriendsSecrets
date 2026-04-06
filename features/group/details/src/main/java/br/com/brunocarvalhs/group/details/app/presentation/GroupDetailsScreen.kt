package br.com.brunocarvalhs.group.details.app.presentation

import androidx.compose.foundation.background
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import br.com.brunocarvalhs.friendssecrets.domain.extensions.toFormattedDate
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.details.app.presentation.components.ActionIconCard
import br.com.brunocarvalhs.group.details.app.presentation.components.MemberItem
import br.com.brunocarvalhs.group.details.app.presentation.components.SectionHeader
import br.com.brunocarvalhs.group.details.app.presentation.components.SettingItem
import coil.compose.AsyncImage

@Composable
fun GroupDetailsScreen(
    viewModel: GroupDetailsViewModel,
    onBack: () -> Unit = {},
    onChat: () -> Unit = {},
    onDraw: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val group = uiState.group

    GroupDetailsContent(
        name = group.name,
        description = group.description.orEmpty(),
        photoUrl = group.photo,
        memberCount = group.members.size,
        createdAtTimestamp = group.createdAt,
        isOwner = group.isOwner,
        isDrawn = group.draws.isNotEmpty(),
        drawDate = group.date,
        minPrice = group.minPrice,
        maxPrice = group.maxPrice,
        giftType = group.type,
        members = group.members,
        draws = group.draws,
        onBack = onBack,
        onDraw = onDraw,
        onChat = onChat,
        onDelete = { viewModel.handleIntent(GroupDetailsIntent.Delete(onBack)) },
        onShareGroup = { viewModel.handleIntent(GroupDetailsIntent.Share) },
        onExit = { viewModel.handleIntent(GroupDetailsIntent.Exit(onBack)) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupDetailsContent(
    name: String,
    description: String,
    photoUrl: String?,
    memberCount: Int,
    createdAtTimestamp: Long,
    isOwner: Boolean,
    isDrawn: Boolean,
    drawDate: String?,
    minPrice: Double?,
    maxPrice: Double?,
    giftType: String?,
    members: List<UserModel>,
    draws: Map<String, String>,
    onBack: () -> Unit,
    onDraw: () -> Unit,
    onChat: () -> Unit,
    onDelete: () -> Unit,
    onExit: () -> Unit,
    onShareGroup: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

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
                            if (isOwner) {
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
                    model = photoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = name.ifBlank { "Grupo sem nome" },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Grupo · $memberCount participantes",
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
                        onClick = onDraw
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Descrição e Data de Criação
            val showDescription = description.isNotBlank() || isOwner
            if (showDescription) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        if (description.isNotBlank() || isOwner) {
                            Text(
                                text = description.ifBlank { "Adicionar descrição ao grupo" },
                                color = if (description.isBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        
                        if (createdAtTimestamp > 0) {
                            Text(
                                text = "Criado em ${createdAtTimestamp.toFormattedDate()}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
                }
            }

            // Detalhes do Sorteio (Apenas se houver dados)
            val hasDrawDetails = drawDate != null || minPrice != null || maxPrice != null || giftType != null
            if (hasDrawDetails) {
                item {
                    SectionHeader(title = "Detalhes do sorteio")
                    drawDate?.let {
                        SettingItem(
                            Icons.Default.CalendarToday,
                            "Data do sorteio",
                            it
                        )
                    }
                    if (minPrice != null || maxPrice != null) {
                        val priceRange = if (minPrice != null && maxPrice != null) {
                            "Entre R$ $minPrice e R$ $maxPrice"
                        } else if (minPrice != null) {
                            "A partir de R$ $minPrice"
                        } else {
                            "Até R$ $maxPrice"
                        }
                        SettingItem(Icons.Default.AttachMoney, "Faixa de preço", priceRange)
                    }
                    giftType?.let { SettingItem(Icons.Default.CardGiftcard, "Tipo de presente", it) }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
                }
            }

            // Participantes
            item {
                SectionHeader(
                    title = "$memberCount participantes",
                )
                if (isOwner && !isDrawn) {
                    SettingItem(
                        Icons.Default.PersonAdd,
                        "Adicionar participantes",
                        iconColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary
                    )
                }
            }

            items(members) { member ->
                MemberItem(
                    participant = member.name,
                    draws = draws,
                    isAdministrator = isOwner,
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
                if (isOwner) {
                    SettingItem(Icons.Default.Edit, "Editar informações do grupo")
                }
                SettingItem(
                    icon = if (isOwner) Icons.Default.Delete else Icons.AutoMirrored.Filled.ExitToApp,
                    title = if (isOwner) "Apagar grupo" else "Sair do grupo",
                    textColor = MaterialTheme.colorScheme.error,
                    iconColor = MaterialTheme.colorScheme.error,
                    onClick = if (isOwner) onDelete else onExit
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupDetailsPreview() {
    GroupDetailsContent(
        name = "Amigo Secreto da Família",
        description = "Troca de presentes de Natal 2024. Vamos fazer algo bem legal este ano!",
        photoUrl = null,
        memberCount = 4,
        createdAtTimestamp = System.currentTimeMillis(),
        isOwner = true,
        isDrawn = false,
        drawDate = "20/12/2024",
        minPrice = 50.0,
        maxPrice = 200.0,
        giftType = "Qualquer coisa",
        members = emptyList(),
        draws = emptyMap(),
        onBack = {},
        onDraw = {},
        onChat = {},
        onDelete = {},
        onExit = {},
        onShareGroup = {},
    )
}
