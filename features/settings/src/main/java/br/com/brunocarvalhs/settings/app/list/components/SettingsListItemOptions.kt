package br.com.brunocarvalhs.settings.app.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ForwardToInbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

/**
 * A titled switch row. [selected] is read directly from the caller on every recomposition
 * (rather than snapshotted into internal state), so it stays in sync when something other than
 * this switch changes the underlying value - e.g. two mutually-exclusive switches where turning
 * one on programmatically turns the other off.
 */
@Composable
internal fun SettingsListItemOptions(
    title: String,
    icon: ImageVector,
    selected: Boolean = false,
    onClick: (Boolean) -> Unit,
) {
    ListItem(
        modifier = Modifier
            .clickable { onClick(!selected) }
            .selectableGroup(),
        headlineContent = { Text(title) },
        trailingContent = {
            Switch(
                checked = selected,
                onCheckedChange = onClick
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = title,
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
