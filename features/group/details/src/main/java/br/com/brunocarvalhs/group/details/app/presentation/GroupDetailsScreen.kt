package br.com.brunocarvalhs.group.details.app.presentation

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import br.com.brunocarvalhs.core.domain.extensions.toCurrencyMask
import br.com.brunocarvalhs.core.domain.extensions.toFormattedDate
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.group.details.R
import br.com.brunocarvalhs.group.details.app.presentation.components.ActionIconCard
import br.com.brunocarvalhs.group.details.app.presentation.components.AddAdjectiveDialog
import br.com.brunocarvalhs.group.details.app.presentation.components.EditLikesDialog
import br.com.brunocarvalhs.group.details.app.presentation.components.MemberItem
import br.com.brunocarvalhs.group.details.app.presentation.components.SectionHeader
import br.com.brunocarvalhs.group.details.app.presentation.components.SettingItem
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun GroupDetailsScreen(
    viewModel: GroupDetailsViewModel,
    onBack: () -> Unit = {},
    onChat: (GroupModel) -> Unit = {},
    onDraw: (GroupModel) -> Unit = {},
    onEdit: (GroupModel) -> Unit = {},
    onAddMembers: (GroupModel) -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()
    val group = uiState.group
    var memberPendingRemoval by remember { mutableStateOf<UserModel?>(null) }
    var editingLikesMember by remember { mutableStateOf<UserModel?>(null) }
    var addingAdjectiveMember by remember { mutableStateOf<UserModel?>(null) }

    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.handleIntent(GroupDetailsIntent.Refresh)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
        currentDeviceId = uiState.currentDeviceId,
        onBack = onBack,
        onDraw = { onDraw.invoke(group) },
        onChat = { onChat.invoke(group) },
        onDelete = { viewModel.handleIntent(GroupDetailsIntent.Delete(onBack)) },
        onShareGroup = { viewModel.handleIntent(GroupDetailsIntent.Share) },
        onShareInviteCard = { viewModel.handleIntent(GroupDetailsIntent.ShareInviteCard) },
        onShareQrCode = { viewModel.handleIntent(GroupDetailsIntent.ShareQr) },
        onExit = { viewModel.handleIntent(GroupDetailsIntent.Exit(onBack)) },
        onEdit = { onEdit.invoke(group) },
        onAddMembers = { onAddMembers.invoke(group) },
        isReminderEnabled = uiState.isReminderEnabled,
        onToggleReminder = { enabled ->
            if (enabled &&
                notificationPermissionState != null &&
                !notificationPermissionState.status.isGranted
            ) {
                notificationPermissionState.launchPermissionRequest()
            }
            viewModel.handleIntent(GroupDetailsIntent.ToggleReminder(enabled))
        },
        onRemoveMember = { member -> memberPendingRemoval = member },
        onShareWishlist = { viewModel.handleIntent(GroupDetailsIntent.ShareWishlist) },
        onEditLikes = { member -> editingLikesMember = member },
        onAddAdjective = { member -> addingAdjectiveMember = member }
    )

    memberPendingRemoval?.let { member ->
        AlertDialog(
            onDismissRequest = { memberPendingRemoval = null },
            title = { Text(stringResource(R.string.remove_participant_confirmation_title)) },
            text = {
                Text(
                    stringResource(R.string.remove_participant_confirmation_message, member.name)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.handleIntent(GroupDetailsIntent.RemoveMember(member.id))
                    memberPendingRemoval = null
                }) {
                    Text(stringResource(R.string.remove_participant_confirmation_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { memberPendingRemoval = null }) {
                    Text(stringResource(R.string.remove_participant_confirmation_cancel))
                }
            }
        )
    }

    editingLikesMember?.let { member ->
        EditLikesDialog(
            initialLikes = member.likes,
            onDismiss = { editingLikesMember = null },
            onSave = { likes ->
                viewModel.handleIntent(GroupDetailsIntent.UpdateLikes(likes))
                editingLikesMember = null
            }
        )
    }

    addingAdjectiveMember?.let { member ->
        AddAdjectiveDialog(
            participant = member.name,
            onDismiss = { addingAdjectiveMember = null },
            onSave = { adjective ->
                viewModel.handleIntent(GroupDetailsIntent.AddAdjective(member.id, adjective))
                addingAdjectiveMember = null
            }
        )
    }
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
    onBack: () -> Unit,
    onDraw: () -> Unit,
    onChat: () -> Unit,
    onDelete: () -> Unit,
    onExit: () -> Unit,
    onShareGroup: () -> Unit,
    onShareInviteCard: () -> Unit,
    onShareQrCode: () -> Unit,
    onEdit: () -> Unit,
    onAddMembers: () -> Unit,
    isReminderEnabled: Boolean = false,
    onToggleReminder: (Boolean) -> Unit = {},
    onRemoveMember: (UserModel) -> Unit = {},
    onShareWishlist: () -> Unit = {},
    currentDeviceId: String = "",
    onEditLikes: (UserModel) -> Unit = {},
    onAddAdjective: (UserModel) -> Unit = {},
) {
    Scaffold(
        topBar = {
            GroupDetailsTopBar(onBack = onBack)
        }
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                GroupDetailsHeader(
                    name = name,
                    photoUrl = photoUrl,
                    memberCount = memberCount
                )
            }

            item {
                GroupDetailsActions(
                    isDrawn = isDrawn,
                    onShareGroup = onShareGroup,
                    onShareInviteCard = onShareInviteCard,
                    onShareQrCode = onShareQrCode,
                    onChat = onChat,
                    onDraw = onDraw
                )
            }

            item {
                GroupDetailsDescription(
                    description = description,
                    isOwner = isOwner,
                    createdAtTimestamp = createdAtTimestamp
                )
            }

            item {
                GroupDrawDetails(
                    drawDate = drawDate,
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    giftType = giftType,
                    isReminderEnabled = isReminderEnabled,
                    onToggleReminder = onToggleReminder
                )
            }

            item {
                SectionHeader(
                    title = stringResource(R.string.participants, memberCount),
                )
                if (isOwner && !isDrawn) {
                    SettingItem(
                        Icons.Default.PersonAdd,
                        stringResource(R.string.add_participants),
                        iconColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary,
                        onClick = onAddMembers
                    )
                }
            }

            items(members) { member ->
                val isCurrentUser = currentDeviceId.isNotBlank() &&
                    (member.id == currentDeviceId || member.phoneNumber == currentDeviceId)
                MemberItem(
                    participant = member.name,
                    likes = member.likes,
                    adjectives = member.adjectives.values.flatten().distinct(),
                    isAdministrator = isOwner,
                    onRemove = if (isOwner && !isDrawn) {
                        { onRemoveMember(member) }
                    } else {
                        null
                    },
                    onEdit = if (isCurrentUser) {
                        { onEditLikes(member) }
                    } else {
                        null
                    },
                    onAddAdjective = if (isCurrentUser) {
                        null
                    } else {
                        { onAddAdjective(member) }
                    },
                )
            }

            item {
                GroupDetailsFooter(
                    isOwner = isOwner,
                    onEdit = onEdit,
                    onDelete = onDelete,
                    onExit = onExit,
                    onShareWishlist = onShareWishlist
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupDetailsTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
private fun GroupDetailsHeader(
    name: String,
    photoUrl: String?,
    memberCount: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            text = name.ifBlank { stringResource(R.string.unnamed_group) },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.group_participants, memberCount),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun GroupDetailsActions(
    isDrawn: Boolean,
    onShareGroup: () -> Unit,
    onShareInviteCard: () -> Unit,
    onShareQrCode: () -> Unit,
    onChat: () -> Unit,
    onDraw: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionIconCard(
                Icons.Default.Share,
                stringResource(R.string.invite), onShareGroup
            )
            ActionIconCard(
                Icons.Default.Wallpaper,
                stringResource(R.string.invite_card), onShareInviteCard
            )
            ActionIconCard(
                Icons.Default.QrCode,
                stringResource(R.string.qr_code), onShareQrCode
            )
            ActionIconCard(
                Icons.AutoMirrored.Filled.Chat,
                stringResource(R.string.chat), onChat
            )
            ActionIconCard(
                icon = Icons.Default.Casino,
                label = if (isDrawn) {
                    stringResource(R.string.reveal)
                } else {
                    stringResource(R.string.draw)
                },
                onClick = onDraw
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun GroupDetailsDescription(
    description: String,
    isOwner: Boolean,
    createdAtTimestamp: Long
) {
    if (description.isNotBlank() || isOwner) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = description.ifBlank { stringResource(R.string.add_group_description) },
                    color = if (description.isBlank()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    style = MaterialTheme.typography.bodyLarge
                )

                if (createdAtTimestamp > 0) {
                    Text(
                        text = stringResource(
                            R.string.created_at,
                            createdAtTimestamp.toFormattedDate()
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
private fun GroupDrawDetails(
    drawDate: String?,
    minPrice: Double?,
    maxPrice: Double?,
    giftType: String?,
    isReminderEnabled: Boolean = false,
    onToggleReminder: (Boolean) -> Unit = {},
) {
    val hasDrawDetails = drawDate != null || minPrice != null || maxPrice != null || giftType != null
    if (hasDrawDetails) {
        Column {
            SectionHeader(title = stringResource(R.string.draw_details))
            drawDate?.let {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        SettingItem(
                            Icons.Default.CalendarToday,
                            stringResource(R.string.draw_date),
                            it
                        )
                    }
                    IconButton(onClick = { onToggleReminder(!isReminderEnabled) }) {
                        Icon(
                            imageVector = if (isReminderEnabled) {
                                Icons.Default.NotificationsActive
                            } else {
                                Icons.Default.NotificationsNone
                            },
                            contentDescription = stringResource(
                                if (isReminderEnabled) {
                                    R.string.reminder_disable_action
                                } else {
                                    R.string.reminder_enable_action
                                }
                            ),
                            tint = if (isReminderEnabled) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
            if (minPrice != null || maxPrice != null) {
                val priceRange = if (minPrice != null && maxPrice != null) {
                    stringResource(
                        R.string.price_between_min_and_max,
                        minPrice.toCurrencyMask(),
                        maxPrice.toCurrencyMask()
                    )
                } else if (minPrice != null) {
                    stringResource(R.string.price_from_min, minPrice.toCurrencyMask())
                } else {
                    stringResource(R.string.price_up_to, maxPrice.toCurrencyMask())
                }
                SettingItem(
                    Icons.Default.AttachMoney,
                    stringResource(R.string.price_range), priceRange
                )
            }
            giftType?.let {
                SettingItem(
                    Icons.Default.CardGiftcard,
                    stringResource(R.string.gift_type),
                    it
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
private fun GroupDetailsFooter(
    isOwner: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onExit: () -> Unit,
    onShareWishlist: () -> Unit = {}
) {
    Column {
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
        SettingItem(
            Icons.Default.CardGiftcard,
            stringResource(R.string.share_wishlist_action),
            onClick = onShareWishlist
        )
        if (isOwner) {
            SettingItem(
                Icons.Default.Edit,
                stringResource(R.string.edit_group_information), onClick = onEdit
            )
        }
        SettingItem(
            icon = if (isOwner) Icons.Default.Delete else Icons.AutoMirrored.Filled.ExitToApp,
            title = if (isOwner) {
                stringResource(R.string.delete_group)
            } else {
                stringResource(R.string.leave_group)
            },
            textColor = MaterialTheme.colorScheme.error,
            iconColor = MaterialTheme.colorScheme.error,
            onClick = if (isOwner) onDelete else onExit
        )
        Spacer(modifier = Modifier.height(32.dp))
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
        onBack = {},
        onDraw = {},
        onChat = {},
        onDelete = {},
        onExit = {},
        onShareGroup = {},
        onShareInviteCard = {},
        onShareQrCode = {},
        onEdit = {},
        onAddMembers = {}
    )
}
