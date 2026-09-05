package br.com.brunocarvalhs.settings.app.appearence.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.core.ui.theme.AppPalette
import br.com.brunocarvalhs.settings.R

/**
 * Row of tappable color swatches letting the user pick one of the pre-selected [AppPalette]s.
 * [AppPalette.CUSTOM] is deliberately excluded - it's entered via a dedicated switch instead
 * (see `CustomThemeSwitch`/[br.com.brunocarvalhs.settings.app.appearence.AppearanceIntent.SetCustomThemeEnabled]),
 * since picking it isn't a single tap but a whole color-building flow.
 *
 * When [enabled] is false (e.g. because Dynamic Theme is active and overrides any manual
 * palette), swatches are dimmed and non-interactive, matching how [ThemeSelect] and the
 * dynamic-theme toggle are conditionally shown/enabled in AppearanceContent.
 */
@Composable
internal fun PaletteSelect(
    modifier: Modifier = Modifier,
    selected: String = AppPalette.Default.id,
    enabled: Boolean = true,
    onClick: (String) -> Unit = {},
) {
    val palettes = remember { AppPalette.entries.filter { it != AppPalette.CUSTOM } }

    LazyRow(
        modifier = modifier.selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(items = palettes, key = { it.id }) { palette ->
            PaletteSwatch(
                palette = palette,
                isSelected = palette.id == selected,
                enabled = enabled,
                onClick = { onClick(palette.id) }
            )
        }
    }
}

@Composable
private fun PaletteSwatch(
    palette: AppPalette,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val name = stringResource(id = paletteDisplayNameRes(palette))
    val primaryColor = palette.lightColorScheme.primary
    val accentColor = palette.lightColorScheme.tertiary
    val contentAlpha = if (enabled) 1f else DISABLED_ALPHA

    Column(
        modifier = Modifier
            .alpha(contentAlpha)
            .clickable(enabled = enabled, onClick = onClick)
            .semantics(mergeDescendants = true) {
                this.contentDescription = name
                this.role = Role.RadioButton
                this.selected = isSelected
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(SWATCH_SIZE)
                .border(
                    width = if (isSelected) SELECTED_BORDER_WIDTH else UNSELECTED_BORDER_WIDTH,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    shape = CircleShape
                )
                .padding(4.dp)
                .clip(CircleShape)
                .background(primaryColor),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(ACCENT_DOT_SIZE)
                    .border(width = 1.dp, color = Color.White, shape = CircleShape)
                    .clip(CircleShape)
                    .background(accentColor)
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = if (primaryColor.luminance() > LUMINANCE_THRESHOLD) Color.Black else Color.White,
                    modifier = Modifier.size(CHECK_ICON_SIZE)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}

private fun paletteDisplayNameRes(palette: AppPalette): Int = when (palette) {
    AppPalette.CLASSIC -> R.string.palette_classic
    AppPalette.FESTIVE -> R.string.palette_festive
    AppPalette.TROPICAL -> R.string.palette_tropical
    AppPalette.OCEAN -> R.string.palette_ocean
    AppPalette.BERRY -> R.string.palette_berry
    AppPalette.MIDNIGHT_GOLD -> R.string.palette_midnight_gold
    AppPalette.CUSTOM -> R.string.palette_custom
}

private val SWATCH_SIZE = 52.dp
private val SELECTED_BORDER_WIDTH = 3.dp
private val UNSELECTED_BORDER_WIDTH = 1.dp
private val ACCENT_DOT_SIZE = 16.dp
private val CHECK_ICON_SIZE = 20.dp
private const val DISABLED_ALPHA = 0.4f
private const val LUMINANCE_THRESHOLD = 0.5f

@Composable
@Preview
internal fun PaletteSelectPreview() {
    PaletteSelect(selected = AppPalette.FESTIVE.id)
}

@Composable
@Preview
internal fun PaletteSelectDisabledPreview() {
    PaletteSelect(selected = AppPalette.CLASSIC.id, enabled = false)
}
