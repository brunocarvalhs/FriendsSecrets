package br.com.brunocarvalhs.settings.app.list.components

import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ForwardToInbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun SettingsListItemOptions(
    title: String,
    icon: ImageVector,
    selected: Boolean = false,
    onClick: (Boolean) -> Unit,
) {
    var checked by rememberSaveable { mutableStateOf(value = selected) }

    fun onToggle(value: Boolean) {
        checked = value
        onClick.invoke(value)
    }

    ListItem(
        modifier = Modifier
            .toggleable(
                value = checked,
                role = Role.Switch,
                onValueChange = { onToggle(it) }
            )
            .semantics {
                // WCAG: Limpamos para que o TalkBack leia como um único controle consolidado
                toggleableState = if (checked) ToggleableState.On else ToggleableState.Off
            },
        headlineContent = { Text(title) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = null // WCAG: O clique é tratado pelo ListItem pai
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null, // WCAG: O título já é autoexplicativo
            )
        }
    )
    HorizontalDivider()
}

@Composable
@Preview
internal fun SettingsListItemOptionsPreview() {
    SettingsListItemOptions(
        icon = Icons.AutoMirrored.Filled.ForwardToInbox,
        title = "Notifications",
        onClick = {}
    )
}
