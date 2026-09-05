package br.com.brunocarvalhs.settings.app.appearence

import android.os.Build
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.sharp.Style
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunocarvalhs.core.ui.theme.AppPalette
import br.com.brunocarvalhs.settings.R
import br.com.brunocarvalhs.settings.app.appearence.components.CustomColorPicker
import br.com.brunocarvalhs.settings.app.appearence.components.PaletteSelect
import br.com.brunocarvalhs.settings.app.appearence.components.ThemeSelect
import br.com.brunocarvalhs.settings.app.list.components.SettingsListItemOptions

@Composable
internal fun AppearanceScreen(
    viewModel: AppearanceViewModel,
    onBack: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    AppearanceContent(
        themeSelected = state.value.themeSelected,
        isDynamicThemeEnabled = state.value.isDynamicThemeEnabled,
        paletteSelected = state.value.paletteSelected,
        customPrimaryColor = Color(state.value.customPrimaryColor),
        customSecondaryColor = Color(state.value.customSecondaryColor),
        onBack = onBack,
        onDynamicTheme = {
            viewModel.handleIntent(AppearanceIntent.SetDynamicThemeEnabled(it))
        },
        onTheme = {
            viewModel.handleIntent(AppearanceIntent.SetTheme(it))
        },
        onPalette = {
            viewModel.handleIntent(AppearanceIntent.SetPalette(it))
        },
        onCustomTheme = {
            viewModel.handleIntent(AppearanceIntent.SetCustomThemeEnabled(it))
        },
        onCustomPrimaryColor = {
            viewModel.handleIntent(
                AppearanceIntent.SetCustomColors(it.toArgb(), state.value.customSecondaryColor)
            )
        },
        onCustomSecondaryColor = {
            viewModel.handleIntent(
                AppearanceIntent.SetCustomColors(state.value.customPrimaryColor, it.toArgb())
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppearanceContent(
    themeSelected: String,
    isDynamicThemeEnabled: Boolean = false,
    paletteSelected: String = AppPalette.Default.id,
    customPrimaryColor: Color = AppPalette.Default.lightColorScheme.primary,
    customSecondaryColor: Color = AppPalette.Default.lightColorScheme.secondary,
    onBack: () -> Unit = {},
    onDynamicTheme: (Boolean) -> Unit = {},
    onTheme: (String) -> Unit = {},
    onPalette: (String) -> Unit = {},
    onCustomTheme: (Boolean) -> Unit = {},
    onCustomPrimaryColor: (Color) -> Unit = {},
    onCustomSecondaryColor: (Color) -> Unit = {}
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                title = {
                    Text(text = stringResource(R.string.title_appearance))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            item {
                Text(text = stringResource(R.string.appearance_screen_title))
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                ThemeSelect(
                    modifier = Modifier.fillMaxWidth(),
                    selected = themeSelected,
                    onClick = onTheme,
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(
                        if (isDynamicThemeEnabled) {
                            R.string.appearance_screen_palette_title_disabled
                        } else {
                            R.string.appearance_screen_palette_title
                        }
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                PaletteSelect(
                    modifier = Modifier.fillMaxWidth(),
                    selected = paletteSelected,
                    enabled = !isDynamicThemeEnabled,
                    onClick = onPalette,
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsListItemOptions(
                    selected = paletteSelected == AppPalette.CUSTOM.id,
                    title = stringResource(R.string.appearance_screen_custom_theme_title),
                    icon = Icons.Filled.Palette,
                    onClick = onCustomTheme
                )
                if (paletteSelected == AppPalette.CUSTOM.id) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomColorPicker(
                        modifier = Modifier.fillMaxWidth(),
                        primaryColor = customPrimaryColor,
                        secondaryColor = customSecondaryColor,
                        onPrimaryColorChange = onCustomPrimaryColor,
                        onSecondaryColorChange = onCustomSecondaryColor,
                    )
                }
            }
            item {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = stringResource(R.string.appearance_screen_title_description))
                    Spacer(modifier = Modifier.height(24.dp))
                    SettingsListItemOptions(
                        selected = isDynamicThemeEnabled,
                        title = stringResource(R.string.appearance_screen_dynamic_title),
                        icon = Icons.Sharp.Style,
                        onClick = onDynamicTheme
                    )
                }
            }
        }
    }
}


@Composable
@Preview
internal fun AppearanceContentPreview() {
    AppearanceContent(
        themeSelected = "Light",
        isDynamicThemeEnabled = true,
        paletteSelected = AppPalette.Default.id,
        onBack = {},
        onDynamicTheme = {},
        onTheme = {},
        onPalette = {}
    )
}

@Composable
@Preview
internal fun AppearanceContentPalettePreview() {
    AppearanceContent(
        themeSelected = "Light",
        isDynamicThemeEnabled = false,
        paletteSelected = AppPalette.TROPICAL.id,
        onBack = {},
        onDynamicTheme = {},
        onTheme = {},
        onPalette = {}
    )
}

@Composable
@Preview
internal fun AppearanceContentCustomPalettePreview() {
    AppearanceContent(
        themeSelected = "Light",
        isDynamicThemeEnabled = false,
        paletteSelected = AppPalette.CUSTOM.id,
        onBack = {},
        onDynamicTheme = {},
        onTheme = {},
        onPalette = {}
    )
}

