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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.core.ui.theme.CUSTOM_COLOR_SWATCHES
import br.com.brunocarvalhs.settings.R

/**
 * Lets the user build a [br.com.brunocarvalhs.core.ui.theme.AppPalette.CUSTOM] theme by picking
 * a primary and a secondary color, each from [CUSTOM_COLOR_SWATCHES]. The two picks are
 * independent, so any combination is possible.
 */
@Composable
internal fun CustomColorPicker(
    modifier: Modifier = Modifier,
    primaryColor: Color,
    secondaryColor: Color,
    onPrimaryColorChange: (Color) -> Unit,
    onSecondaryColorChange: (Color) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.appearance_screen_custom_primary_label),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        ColorSwatchRow(selected = primaryColor, onClick = onPrimaryColorChange)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.appearance_screen_custom_secondary_label),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        ColorSwatchRow(selected = secondaryColor, onClick = onSecondaryColorChange)
    }
}

@Composable
private fun ColorSwatchRow(selected: Color, onClick: (Color) -> Unit) {
    LazyRow(
        modifier = Modifier.selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(items = CUSTOM_COLOR_SWATCHES, key = { it.toArgb() }) { color ->
            ColorSwatch(
                color = color,
                isSelected = color == selected,
                onClick = { onClick(color) }
            )
        }
    }
}

@Composable
private fun ColorSwatch(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(SWATCH_SIZE)
            .border(
                width = if (isSelected) SELECTED_BORDER_WIDTH else UNSELECTED_BORDER_WIDTH,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
            .padding(4.dp)
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick)
            .semantics {
                this.contentDescription = "#%06X".format(color.toArgb() and 0xFFFFFF)
                this.role = Role.RadioButton
                this.selected = isSelected
            },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = if (color.luminance() > 0.5f) Color.Black else Color.White,
                modifier = Modifier.size(CHECK_ICON_SIZE)
            )
        }
    }
}

private val SWATCH_SIZE = 40.dp
private val SELECTED_BORDER_WIDTH = 3.dp
private val UNSELECTED_BORDER_WIDTH = 1.dp
private val CHECK_ICON_SIZE = 18.dp

@Composable
@Preview
internal fun CustomColorPickerPreview() {
    CustomColorPicker(
        primaryColor = CUSTOM_COLOR_SWATCHES[7],
        secondaryColor = CUSTOM_COLOR_SWATCHES[1],
        onPrimaryColorChange = {},
        onSecondaryColorChange = {}
    )
}
