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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import br.com.brunocarvalhs.settings.R

@Composable
internal fun SettingsListItemNavigation(
    title: Int,
    icon: ImageVector,
    onClick: () -> Unit = {},
) {
    ListItem(
        modifier = Modifier
            .clickable(
                onClickLabel = stringResource(R.string.open),
                onClick = onClick
            )
            .semantics {
                role = Role.Button
            },
        headlineContent = { Text(stringResource(title)) },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null, // WCAG: Decorativo, já que o contexto é navegação
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null, // WCAG: O título já descreve a seção
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
