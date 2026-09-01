package br.com.brunocarvalhs.group.list.app.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import br.com.brunocarvalhs.group.list.R
import br.com.brunocarvalhs.group.list.commons.options.OptionsMore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GroupListAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onJoinGroupClick: () -> Unit,
    moreOptions: List<OptionsMore>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    isJoinGroupEnabled: Boolean = true,
) {
    TopAppBar(
        title = { Text("Friends Secrets", style = MaterialTheme.typography.titleLarge) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Companion.Transparent
        ),
        actions = {
            if (isJoinGroupEnabled) {
                IconButton(onClick = onJoinGroupClick) {
                    Icon(
                        Icons.Default.GroupAdd,
                        contentDescription = stringResource(R.string.join_group)
                    )
                }
            }

            if (moreOptions.isNotEmpty()) {
                IconButton(onClick = { onExpandedChange(true) }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.more_options)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) }
                ) {
                    moreOptions.forEach { option ->
                        DropdownMenuItem(
                            leadingIcon = {
                                option.icon?.let {
                                    Icon(
                                        imageVector = option.icon,
                                        contentDescription = option.contentDescription()
                                    )
                                }
                            },
                            text = { Text(text = option.name()) },
                            onClick = {
                                onExpandedChange(false)
                                option.lambda()
                            }
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}
