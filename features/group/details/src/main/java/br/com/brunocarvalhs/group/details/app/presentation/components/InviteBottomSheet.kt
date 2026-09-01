package br.com.brunocarvalhs.group.details.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.details.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteBottomSheet(
    onDismiss: () -> Unit,
    onShareLink: () -> Unit,
    onShareQrCode: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun dismissAndRun(action: () -> Unit) {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss()
            }
            action()
        }
    }

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.invite),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            InviteOptionRow(
                icon = Icons.Default.Share,
                title = stringResource(R.string.invite_share_link_title),
                description = stringResource(R.string.invite_share_link_description),
                onClick = { dismissAndRun(onShareLink) }
            )
            InviteOptionRow(
                icon = Icons.Default.QrCode,
                title = stringResource(R.string.qr_code),
                description = stringResource(R.string.invite_share_qr_description),
                onClick = { dismissAndRun(onShareQrCode) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InviteOptionRow(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
