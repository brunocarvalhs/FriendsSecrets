package br.com.brunocarvalhs.group.app.details.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.ui.fake.toFake
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.group.R
import br.com.brunocarvalhs.group.app.details.ExpandableText
import br.com.brunocarvalhs.group.app.details.GroupDetailsUiState

@Composable
fun GroupInfoTab(
    uiState: GroupDetailsUiState.Success,
    onEditGroup: ((GroupEntities) -> Unit)? = null,
    onInviteMembers: ((GroupEntities) -> Unit)? = null,
    onConfigureDraw: ((GroupEntities) -> Unit)? = null,
    onSharedGroup: ((GroupEntities) -> Unit)? = null,
    onDraw: (GroupEntities) -> Unit = {},
    revelationDraw: (GroupEntities) -> Unit = {}
) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Descrição do grupo
            item {
                uiState.group.description?.let { description ->
                    Column {
                        Text(
                            text = stringResource(id = R.string.group_details_description),
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ExpandableText(text = description, maxLines = 3)
                    }
                }
            }

            // Detalhes do Sorteio
            item {
                DrawDetailsSection(
                    date = "24/12/2025 às 18:00",
                    minValue = "R$ 50,00",
                    maxValue = "R$ 150,00",
                    type = "Temático – Filmes Preferidos"
                )
            }

            item {
                DrawActionSection(
                    isDrawn = uiState.group.draws.isNotEmpty(),
                    onClick = {
                        if (uiState.group.draws.isNotEmpty()) {
                            revelationDraw(uiState.group)
                        } else {
                            onDraw(uiState.group)
                        }
                    }
                )
            }

            // Título da seção de gerenciamento
            item {
                Text(
                    text = "Gerenciamento",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            onSharedGroup?.let {
                item {
                    ManagementCard(
                        icon = Icons.Default.Share,
                        title = "Compartilhar Grupo",
                        description = "Envie o link do grupo para amigos entrarem",
                        onClick = { onSharedGroup(uiState.group) }
                    )
                }
            }

            // Cards de gerenciamento
            onEditGroup?.let {
                item {
                    ManagementCard(
                        icon = Icons.Default.Edit,
                        title = "Editar Grupo",
                        description = "Altere nome, descrição e configurações",
                        onClick = { onEditGroup(uiState.group) }
                    )
                }
            }

            onInviteMembers?.let {
                item {
                    ManagementCard(
                        icon = Icons.Default.PersonAdd,
                        title = "Convidar Membros",
                        description = "Envie convites para seus amigos",
                        onClick = { onInviteMembers(uiState.group) }
                    )
                }
            }

            onConfigureDraw?.let {
                item {
                    ManagementCard(
                        icon = Icons.Default.Settings,
                        title = "Configurar Sorteio",
                        description = "Defina regras e datas para o amigo secreto",
                        onClick = { onConfigureDraw(uiState.group) }
                    )
                }
            }
        }
    }
}

@Composable
fun DrawActionSection(
    isDrawn: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (isDrawn) Icons.Default.CardGiftcard else Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = if (isDrawn) "Revelar Amigo Secreto" else "Pronto para sortear?",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = if (isDrawn)
                    "Está na hora de descobrir quem tirou quem!"
                else
                    "Clique abaixo para realizar o sorteio dos participantes.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isDrawn) "Revelar" else "Sortear",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun ManagementCard(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DrawDetailsSection(
    date: String,
    minValue: String,
    maxValue: String,
    type: String
) {
    Column {
        Text(
            text = "Detalhes do Sorteio",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        DrawDetailCard(
            icon = Icons.Default.CalendarToday,
            label = "Data do Sorteio",
            value = date
        )
        DrawDetailCard(
            icon = Icons.Default.AttachMoney,
            label = "Valor Mínimo",
            value = minValue
        )
        DrawDetailCard(
            icon = Icons.Default.MoneyOff,
            label = "Valor Máximo",
            value = maxValue
        )
        DrawDetailCard(
            icon = Icons.Default.CardGiftcard,
            label = "Tipo de Amigo Secreto",
            value = type
        )
    }
}

@Composable
fun DrawDetailCard(
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(28.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


@Composable
@Preview
private fun GroupInfoTabPreview() {
    FriendsSecretsTheme {
        GroupInfoTab(uiState = GroupDetailsUiState.Success(GroupEntities.toFake()))
    }
}
