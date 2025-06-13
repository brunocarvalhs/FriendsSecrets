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
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
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
    onSharedGroup: ((GroupEntities) -> Unit)? = null,
    onDraw: (GroupEntities) -> Unit = {},
    revelationDraw: (GroupEntities) -> Unit = {},
    onExitGroup: ((GroupEntities) -> Unit)? = null,
    onDeleteGroup: ((GroupEntities) -> Unit)? = null,
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

            if (uiState.group.date != null) {
                item {
                    DrawDetailsSection(
                        group = uiState.group
                    )
                }
            }

            item {
                Text(
                    text = stringResource(R.string.gerenciamento),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
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

            onSharedGroup?.let {
                item {
                    ManagementCard(
                        icon = Icons.Default.Share,
                        title = stringResource(R.string.compartilhar_grupo),
                        description = stringResource(R.string.envie_o_link_do_grupo_para_amigos_entrarem),
                        onClick = { onSharedGroup(uiState.group) }
                    )
                }
            }

            if (uiState.group.draws.isEmpty()) {
                if (uiState.group.isOwner) {
                    onEditGroup?.let {
                        item {
                            ManagementCard(
                                icon = Icons.Default.Edit,
                                title = stringResource(R.string.editar_grupo),
                                description = stringResource(R.string.altere_nome_descri_o_e_configura_es),
                                onClick = { onEditGroup(uiState.group) }
                            )
                        }
                    }
                }

                if (uiState.group.isOwner) {
                    onDeleteGroup?.let {
                        item {
                            ManagementCard(
                                icon = Icons.Default.DeleteForever,
                                title = stringResource(R.string.group_details_drop_menu_item_text_exit_to_group_admin),
                                description = stringResource(R.string.esta_a_o_apagar_permanentemente_o_grupo_e_todas_as_suas_informa_es_os_participantes_n_o_ter_o_mais_acesso_ao_grupo_esta_a_o_irrevers_vel),
                                onClick = { onDeleteGroup(uiState.group) }
                            )
                        }
                    }
                } else {
                    onExitGroup?.let {
                        item {
                            ManagementCard(
                                icon = Icons.Default.DeleteForever,
                                title = stringResource(R.string.group_details_drop_menu_item_text_exit_to_group),
                                description = stringResource(R.string.voc_ser_removido_deste_grupo_e_n_o_poder_mais_participar_dos_sorteios_ou_visualizar_informa_es),
                                onClick = { onExitGroup(uiState.group) }
                            )
                        }
                    }
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
                text = if (isDrawn) stringResource(R.string.revelar_amigo_secreto) else stringResource(
                    R.string.pronto_para_sortear
                ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            Text(
                text = if (isDrawn)
                    stringResource(R.string.est_na_hora_de_descobrir_quem_tirou_quem)
                else
                    stringResource(R.string.clique_abaixo_para_realizar_o_sorteio_dos_participantes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isDrawn) stringResource(R.string.revelar) else stringResource(R.string.sortear),
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
    group: GroupEntities,
) {
    Column {
        Text(
            text = stringResource(R.string.detalhes_do_sorteio),
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (group.date != null) {
            DrawDetailCard(
                icon = Icons.Default.CalendarToday,
                label = stringResource(R.string.data_do_sorteio),
                value = group.date.orEmpty()
            )
        }
        if (group.minPrice != null) {
            DrawDetailCard(
                icon = Icons.Default.AttachMoney,
                label = stringResource(R.string.valor_m_nimo),
                value = group.minPrice.toString()
            )
        }

        if (group.maxPrice != null) {
            DrawDetailCard(
                icon = Icons.Default.AttachMoney,
                label = stringResource(R.string.valor_m_ximo),
                value = group.maxPrice.toString()
            )
        }

        if (group.type != null) {
            DrawDetailCard(
                icon = Icons.Default.CardGiftcard,
                label = stringResource(R.string.tipo_de_sorteio),
                value = group.type.orEmpty()
            )
        }
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
