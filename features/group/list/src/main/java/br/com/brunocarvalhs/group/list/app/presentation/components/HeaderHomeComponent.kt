package br.com.brunocarvalhs.group.list.app.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.brunocarvalhs.group.list.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
internal fun HeaderHomeComponent(
    firstName: String? = null,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    onAdd: () -> Unit = {},
    onNotification: () -> Unit = {},
    notificationsCount: Int = 0,
) {
    LargeTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
    ), title = {
        val text = firstName?.let {
            "${stringResource(R.string.home_title)} $it"
        } ?: stringResource(R.string.home_title)
        Text(
            text = text, style = MaterialTheme.typography.titleLarge
        )
    }, actions = {
        IconButton(onClick = onAdd) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add"
            )
        }
        IconButton(onClick = onNotification) {
            BadgedBox(badge = {
                if (notificationsCount > 0) {
                    Badge()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Groups,
                    contentDescription = "Groups"
                )
            }
        }
    }, scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun HeaderHomeComponentPreview() {
        HeaderHomeComponent(
            firstName = "Bruno",
        )
}