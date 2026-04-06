package br.com.brunocarvalhs.group.list.app.presentation.details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.brunocarvalhs.group.list.R

@Composable
fun FloatingButtonComponent(
    draws: Map<String, String> = emptyMap(),
    isOwner: Boolean,
    onDraw: () -> Unit = {},
    revelationDraw: () -> Unit = {},
) {
    if (draws.isNotEmpty()) {
        ExtendedFloatingActionButton(onClick = revelationDraw) {
            Icon(
                imageVector = Icons.Filled.People,
                contentDescription = stringResource(R.string.group_details_action_preview_my_secret_friend)
            )
            Text(text = stringResource(R.string.group_details_action_preview_my_secret_friend))
        }
    } else if (isOwner) {
        ExtendedFloatingActionButton(onClick = onDraw) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = stringResource(R.string.group_details_action_draw_members)
            )
            Text(text = stringResource(R.string.group_details_action_draw_members))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FloatingButtonComponentPreview() {
    FloatingButtonComponent(
        draws = emptyMap(),
        isOwner = true,
        onDraw = {},
        revelationDraw = {}
    )
}