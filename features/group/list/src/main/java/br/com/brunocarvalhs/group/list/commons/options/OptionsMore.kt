package br.com.brunocarvalhs.group.list.commons.options

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class OptionsMore(
    val name: @Composable () -> String,
    val icon: ImageVector? = null,
    val contentDescription: @Composable () -> String,
    val lambda: () -> Unit,
)
