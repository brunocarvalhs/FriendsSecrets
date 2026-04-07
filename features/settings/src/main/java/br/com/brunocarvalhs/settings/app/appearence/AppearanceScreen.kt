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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunocarvalhs.settings.R
import br.com.brunocarvalhs.settings.app.appearence.components.ThemeSelect
import br.com.brunocarvalhs.settings.app.list.components.SettingsListItemOptions

@Composable
fun AppearanceScreen(
    viewModel: AppearanceViewModel,
    onBack: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    AppearanceContent(
        themeSelected = state.value.themeSelected,
        isDynamicThemeEnabled = state.value.isDynamicThemeEnabled,
        onBack = onBack,
        onDynamicTheme = {
            viewModel.handleIntent(AppearanceIntent.SetDynamicThemeEnabled(it))
        },
        onTheme = {
            viewModel.handleIntent(AppearanceIntent.SetTheme(it))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppearanceContent(
    themeSelected: String,
    isDynamicThemeEnabled: Boolean = false,
    onBack: () -> Unit = {},
    onDynamicTheme: (Boolean) -> Unit = {},
    onTheme: (String) -> Unit = {}
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(text = stringResource(R.string.title_appearance))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
private fun AppearanceContentPreview() {
    AppearanceContent(
        themeSelected = "Light",
        isDynamicThemeEnabled = true,
        onBack = {},
        onDynamicTheme = {},
        onTheme = {}
    )
}