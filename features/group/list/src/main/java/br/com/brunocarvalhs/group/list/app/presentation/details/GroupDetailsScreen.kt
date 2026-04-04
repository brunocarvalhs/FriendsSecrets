package br.com.brunocarvalhs.group.list.app.presentation.details

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.copy
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.list.R
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.list.app.domain.entities.UserModel
import br.com.brunocarvalhs.group.list.app.presentation.details.components.GroupInfoTab
import br.com.brunocarvalhs.group.list.app.presentation.details.components.MemberItem
import br.com.brunocarvalhs.group.list.app.presentation.details.components.MembersTab
import coil.compose.AsyncImage

@Composable
fun GroupDetailsScreen(
    viewModel: GroupDetailsViewModel,
    onBack: () -> Unit = {},
    onDraw: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    GroupDetailsContent(
        group = uiState.group,
        onBack = onBack,
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
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null
                        )
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
            // Header: Imagem, Nome e Subtítulo
            item {
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    model = null, // Substituir pela URL da imagem do grupo se disponível
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.2f)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Grupo · ${group.members.size} membros",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Botões de Ação Rápidas
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionIconCard(Icons.Default.Mic, "Voz") {}
                    ActionIconCard(Icons.Default.PersonAdd, "Adicionar", onAddMember)
                    ActionIconCard(Icons.Default.Search, "Pesquisar", onSearch)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Descrição e Data de Criação
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = group.description.ifBlank { "Adicionar descrição ao grupo" },
                        color = if (group.description.isBlank()) Color(0xFF00A884) else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Criado em ${group.createdAt ?: "Desconhecido"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
            }

            // Seção de Mídia (Mockup)
            item {
                SectionHeader(title = "Mídia, links e docs", trailing = "536 >")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(5) { // Mockup de itens de mídia
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray.copy(alpha = 0.3f))
                        )
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
            }

            // Configurações≈
            item {
                SettingItem(Icons.Default.Notifications, "Notificações", "Todas")
                SettingItem(Icons.Default.Image, "Visibilidade de mídia")
                SettingItem(Icons.Default.Lock, "Criptografia", "As mensagens são protegidas...")
                Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
            }

            // Lista de Membros
            item {
                SectionHeader(
                    title = "${group.members.size} membros",
                    trailingIcon = Icons.Default.Search
                )
                SettingItem(
                    Icons.Default.PersonAdd,
                    "Adicionar membros",
                    iconColor = Color(0xFF00A884),
                    textColor = Color(0xFF00A884)
                )
                SettingItem(
                    Icons.Default.Link,
                    "Convidar via link",
                    iconColor = Color(0xFF00A884),
                    textColor = Color(0xFF00A884)
                )
            }

            items(group.members) { member ->
                MemberItem(
                    participant = member.name,
                    isAdministrator = group.isOwner, // Simplificação
                    onRemove = onRemoveMember,
                    onEdit = onEditMember
                )
            }

            // Ações de Rodapé
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SettingItem(Icons.Default.FavoriteBorder, "Adicionar aos favoritos")
                SettingItem(
                    Icons.Default.ExitToApp,
                    "Sair do grupo",
                    textColor = Color.Red,
                    iconColor = Color.Red
                )
                SettingItem(
                    Icons.Default.ThumbDown,
                    "Denunciar grupo",
                    textColor = Color.Red,
                    iconColor = Color.Red
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ActionIconCard(icon: ImageVector, label: String, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .border(
                0.5.dp,
                Color.Gray.copy(alpha = 0.3f),
                androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF00A884))
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color(0xFF00A884))
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
        Text(title, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        if (trailing != null) {
            Text(trailing, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        if (trailingIcon != null) {
            Icon(
                trailingIcon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Gray
            )
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
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }
}

internal class GroupDetailsPreviewProvider : PreviewParameterProvider<GroupDetailsUiState> {
    override val values = sequenceOf(
        GroupDetailsUiState(
            group = GroupModel()
        )
    )
}

@Composable
@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
fun GroupDetailsScreenPreview(
    @PreviewParameter(GroupDetailsPreviewProvider::class) state: GroupDetailsUiState,
) {
    GroupDetailsContent(
        group = state.group,
        onBack = {},
        onShareGroup = {},
        onAddMember = {},
        onSearch = {},
        onRemoveMember = {},
        onEditMember = {}
    )
}
