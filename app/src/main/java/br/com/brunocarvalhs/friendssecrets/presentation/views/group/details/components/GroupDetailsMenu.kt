package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.GroupNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsUiState

@Composable
fun GroupDetailsMenu(
    uiState: GroupDetailsUiState.Success,
    navController: NavController = rememberNavController(),
    expanded: Boolean = false,
    setExpanded: (Boolean) -> Unit = {},
    exitGroup: (GroupEntities) -> Unit = {},
    deleteGroup: (GroupEntities) -> Unit = {},
    onDraw: (GroupEntities) -> Unit = {},
    onShareGroup: (GroupEntities) -> Unit = {},
) {
    IconButton(onClick = { setExpanded(true) }) {
        Icon(
            imageVector = Icons.Filled.MoreVert, contentDescription = "More"
        )
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { setExpanded(false) }) {
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

@Composable
@Preview(showSystemUi = true)
private fun GroupDetailsMenuPreview() {
    Row {
        GroupDetailsMenu(
            uiState = GroupDetailsUiState.Success(
                group = GroupModel(name = "Group admin",
                    isOwner = true,
                    description = "Description",
                    members = mutableMapOf<String, String>().apply {
                        repeat(10) {
                            this["Member $it"] = "Secret Santa $it"
                        }
                    }),
            ),
            expanded = true,
            exitGroup = {},
            deleteGroup = {},
            onDraw = {},
            onShareGroup = {}
        )
    }
}