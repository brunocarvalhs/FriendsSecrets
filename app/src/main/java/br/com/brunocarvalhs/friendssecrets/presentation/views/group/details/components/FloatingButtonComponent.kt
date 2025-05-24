package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsPreviewProvider
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsUiState

@Composable
fun FloatingButtonComponent(
    uiState: GroupDetailsUiState,
    onDraw: (GroupEntities) -> Unit = {},
    revelationDraw: (GroupEntities) -> Unit = {},
) {
    if (uiState is GroupDetailsUiState.Success) {
        if (uiState.group.draws.isNotEmpty()) {
            ExtendedFloatingActionButton(onClick = { revelationDraw(uiState.group) }) {
                Icon(
                    imageVector = Icons.Filled.People,
                    contentDescription = stringResource(R.string.group_details_action_preview_my_secret_friend)
                )
                Text(text = stringResource(R.string.group_details_action_preview_my_secret_friend))
            }
        } else if (uiState.group.isOwner && uiState.group.draws.isEmpty()) {
            ExtendedFloatingActionButton(onClick = { onDraw(uiState.group) }) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(R.string.group_details_action_draw_members)
                )
                Text(text = stringResource(R.string.group_details_action_draw_members))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FloatingButtonComponentPreview(
    @PreviewParameter(GroupDetailsPreviewProvider::class) state: GroupDetailsUiState
) {
    FloatingButtonComponent(
        uiState = state,
        onDraw = {},
        revelationDraw = {}
    )
}