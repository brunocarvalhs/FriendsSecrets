package br.com.brunocarvalhs.settings.app.appearence.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.common.theme.ThemeManager.Theme
import br.com.brunocarvalhs.settings.R

@Composable
fun ThemeSelect(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {},
    selected: String = Theme.LIGHT.name,
) {
    val (selectedTheme, setSelectedTheme) = remember { mutableStateOf(selected) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThemeItem(
            modifier = Modifier.weight(1f),
            selected = selectedTheme,
            theme = Theme.LIGHT,
            onClick = { theme ->
                setSelectedTheme(theme)
                onClick(theme)
            }
        )
        ThemeItem(
            modifier = Modifier.weight(1f),
            selected = selectedTheme,
            theme = Theme.DARK,
            onClick = { theme ->
                setSelectedTheme(theme)
                onClick(theme)
            }
        )
        ThemeItem(
            modifier = Modifier.weight(1f),
            selected = selectedTheme,
            theme = Theme.SYSTEM,
            onClick = { theme ->
                setSelectedTheme(theme)
                onClick(theme)
            }
        )
    }
}

@Composable
private fun ThemeItem(
    modifier: Modifier = Modifier,
    selected: String,
    theme: Theme,
    onClick: (String) -> Unit = {},
) {
    val imageRes = remember(theme) {
        when (theme) {
            Theme.LIGHT -> R.drawable.ic_theme_light
            Theme.DARK -> R.drawable.ic_theme_dark
            Theme.SYSTEM -> R.drawable.ic_theme_system
        }
    }

    val descriptionRes = remember(theme) {
        when (theme) {
            Theme.LIGHT -> R.string.tema_claro
            Theme.DARK -> R.string.tema_escuro
            Theme.SYSTEM -> R.string.tema_do_sistema
        }
    }

    val description = stringResource(id = descriptionRes)
    val isSelected = selected == theme.type
    val onThemeClick = remember(theme, onClick) { { onClick(theme.type) } }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onThemeClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = description,
            modifier = Modifier.height(200.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = theme.type)
        Spacer(modifier = Modifier.height(4.dp))
        RadioButton(
            selected = isSelected,
            onClick = onThemeClick,
            modifier = Modifier.semantics(mergeDescendants = true) {
                this.contentDescription = description
            }
        )
    }
}
