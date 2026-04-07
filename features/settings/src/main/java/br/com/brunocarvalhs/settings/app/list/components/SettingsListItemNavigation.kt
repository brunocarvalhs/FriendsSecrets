package br.com.brunocarvalhs.settings.app.list.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.brunocarvalhs.settings.R

@Composable
fun SettingsListItemNavigation(
    title: Int,
    icon: ImageVector,
    onClick: () -> Unit = {},
) {
    ListItem(
        modifier = Modifier.clickable { onClick.invoke() },
        headlineContent = { Text(stringResource(title)) },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Localized description",
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(title),
            )
        }
    )
    HorizontalDivider()
}

@Composable
@Preview
private fun SettingsListItemNavigationPreview() {
    SettingsListItemNavigation(
        title = R.string.settings_screen_security,
        icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        onClick = { }
    )
}