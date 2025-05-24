@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.GroupNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsPreviewProvider
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderComponent(
    uiState: GroupDetailsUiState,
    navController: NavController = rememberNavController(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    exitGroup: (GroupEntities) -> Unit = {},
    deleteGroup: (GroupEntities) -> Unit = {},
    onDraw: (GroupEntities) -> Unit = {},
    onShareGroup: (GroupEntities) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    LargeTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {
            if (uiState is GroupDetailsUiState.Success) {
                Text(uiState.group.name)
            }
        }, actions = {
            if (uiState is GroupDetailsUiState.Success) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert, contentDescription = "More"
                    )
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(text = { Text(stringResource(R.string.group_details_drop_menu_item_text_edit)) },
                        onClick = {
                            navController.navigate(
                                route = GroupNavigation.Edit.createRoute(uiState.group.id)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Edit, contentDescription = null
                            )
                        })
                    DropdownMenuItem(text = {
                        if (uiState.group.isOwner) {
                            Text(stringResource(R.string.group_details_drop_menu_item_text_exit_to_group_admin))
                        } else {
                            Text(stringResource(R.string.group_details_drop_menu_item_text_exit_to_group))
                        }
                    }, onClick = {
                        if (uiState.group.isOwner) {
                            deleteGroup(uiState.group)
                        } else {
                            exitGroup(uiState.group)
                        }
                    }, leadingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = if (uiState.group.isOwner) {
                                stringResource(R.string.group_details_drop_menu_item_text_exit_to_group_admin)
                            } else {
                                stringResource(R.string.group_details_drop_menu_item_text_exit_to_group)
                            }
                        )
                    })
                    if (uiState.group.isOwner) {
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.group_details_action_draw_members)) },
                            onClick = { onDraw(uiState.group) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = stringResource(R.string.group_details_action_draw_members)
                                )
                            },
                        )
                    }
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.group_details_action_shared)) },
                        onClick = { onShareGroup(uiState.group) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = stringResource(R.string.group_details_action_shared)
                            )
                        },
                    )
                }
            }

        }, navigationIcon = {
            NavigationBackIconButton(navController = navController)
        }, scrollBehavior = scrollBehavior
    )
}

@Composable
@Preview
private fun HeaderComponentPreview(
    @PreviewParameter(GroupDetailsPreviewProvider::class) state: GroupDetailsUiState
) {
    HeaderComponent(
        uiState = state,
        exitGroup = {},
        deleteGroup = {},
        onDraw = {},
        onShareGroup = {}
    )
}