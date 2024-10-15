package br.com.brunocarvalhs.friendssecrets.presentation.views.settings.appearence.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.R

internal enum class Theme(val value: String, val image: Int) {
    LIGHT(value = "Light", image = R.drawable.ic_theme_light),
    DARK(value = "Dark", image = R.drawable.ic_theme_dark),
    SYSTEM(value = "System", image = R.drawable.ic_theme_system)
}

@Composable
fun ThemeSelect(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {},
    selected: String = Theme.LIGHT.name,
) {
    val darkTheme = isSystemInDarkTheme() // Verifica se o sistema está no tema escuro
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
            type = Theme.LIGHT,
            onClick = { theme ->
                setSelectedTheme(theme)
                onClick(theme)
            }
        )
        ThemeItem(
            modifier = Modifier.weight(1f),
            selected = selectedTheme,
            type = Theme.DARK,
            onClick = { theme ->
                setSelectedTheme(theme)
                onClick(theme)
            }
        )
        ThemeItem(
            modifier = Modifier.weight(1f),
            selected = selectedTheme,
            type = Theme.SYSTEM,
            onClick = { theme ->
                setSelectedTheme(theme)
                onClick(theme)
            }
        )
    }

    if (selectedTheme == Theme.SYSTEM.name) {
        if (darkTheme) {
            println("Sistema está no modo escuro")
        } else {
            println("Sistema está no modo claro")
        }
    }
}


@Composable
private fun ThemeItem(
    modifier: Modifier = Modifier,
    selected: String,
    type: Theme,
    onClick: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick.invoke(type.name) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = type.image),
            contentDescription = type.value,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = type.value)
        Spacer(modifier = Modifier.height(4.dp))
        RadioButton(
            selected = selected == type.name,
            onClick = { onClick.invoke(type.name) },
            modifier = Modifier.semantics {
                contentDescription = "Localized Description"
            }
        )
    }
}