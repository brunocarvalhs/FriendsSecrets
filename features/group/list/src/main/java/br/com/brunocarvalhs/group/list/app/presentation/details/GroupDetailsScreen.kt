package br.com.brunocarvalhs.group.list.app.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.list.app.presentation.details.components.MemberItem
import coil.compose.AsyncImage

@Composable
fun GroupDetailsScreen(
    viewModel: GroupDetailsViewModel,
    onBack: () -> Unit = {},
    onDraw: () -> Unit = {},
    onReveal: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    GroupDetailsContent(
        group = uiState.group,
        onBack = onBack,
        onDraw = onDraw,
        onReveal = onReveal,
        onShareGroup = { },
        onAddMember = { },
        onSearch = { },
        onRemoveMember = { },
        onEditMember = { }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupDetailsContent(
    group: GroupModel,
    onBack: () -> Unit,
    onDraw: () -> Unit,
    onReveal: () -> Unit,
    onShareGroup: () -> Unit,
    onAddMember: () -> Unit,
    onSearch: () -> Unit,
    onRemoveMember: () -> Unit,
    onEditMember: () -> Unit,
) {
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
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
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
                    model = null,
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

            // Ações Principais
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionIconCard(Icons.Default.Share, "Convidar", onShareGroup)
                    ActionIconCard(Icons.Default.PersonAdd, "Adicionar", onAddMember)
                    ActionIconCard(Icons.Default.Search, "Pesquisar", onSearch)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Descrição
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
                    Text(
                        text = "Criado em ${group.createdAt ?: "Desconhecido"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
            }

            // Card de Ação do Sorteio (Destaque)
            item {
                DrawStatusCard(
                    isDrawn = group.draws.isNotEmpty(),
                    onAction = if (group.draws.isNotEmpty()) onReveal else onDraw,
                    isOwner = group.isOwner
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
            }

            // Detalhes do Sorteio (Valores, Data, etc)
            item {
                SectionHeader(title = "Detalhes do sorteio")
                group.createdAt?.let { SettingItem(Icons.Default.CalendarToday, "Data do sorteio", it) }
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

            // Lista de Participantes
            item {
                SectionHeader(
                    title = "${group.members.size} participantes",
                    trailingIcon = Icons.Default.Search
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
                    onRemove = onRemoveMember,
                    onEdit = onEditMember
                )
            }

            // Opções Adicionais
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
                if (group.isOwner) {
                    SettingItem(Icons.Default.Edit, "Editar informações do grupo")
                    SettingItem(Icons.Default.Delete, "Apagar grupo", textColor = MaterialTheme.colorScheme.error, iconColor = MaterialTheme.colorScheme.error)
                } else {
                    SettingItem(Icons.AutoMirrored.Filled.ExitToApp, "Sair do grupo", textColor = MaterialTheme.colorScheme.error, iconColor = MaterialTheme.colorScheme.error)
                }
                SettingItem(Icons.Default.ThumbDown, "Denunciar grupo", textColor = MaterialTheme.colorScheme.error, iconColor = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun DrawStatusCard(
    isDrawn: Boolean,
    isOwner: Boolean,
    onAction: () -> Unit
) {
    val containerColor = if (isDrawn) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isDrawn) Icons.Default.Casino else Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isDrawn) "Sorteio realizado!" else "Pronto para o sorteio?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isDrawn) "Descubra agora quem é seu amigo secreto." else "Aguarde o administrador realizar o sorteio.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (isDrawn || isOwner) {
                Button(
                    onClick = onAction,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (isDrawn) "Ver meu amigo" else "Sortear")
                }
            }
        }
    }
}

@Composable
fun ActionIconCard(icon: ImageVector, label: String, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun SectionHeader(title: String, trailing: String? = null, trailingIcon: ImageVector? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
        if (trailing != null) {
            Text(trailing, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (trailingIcon != null) {
            Icon(trailingIcon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = textColor)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
