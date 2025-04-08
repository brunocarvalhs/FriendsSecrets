package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactMemberBottomSheet(
    onDismiss: () -> Unit,
    contacts: List<UserEntities> = emptyList(),
    onMemberAdded: (String, List<String>) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier
            .imePadding(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        ContactMemberContent(
            sheetState = sheetState,
            contacts = contacts,
            onDismiss = onDismiss,
            onMemberAdded = onMemberAdded
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactMemberContent(
    sheetState: SheetState,
    contacts: List<UserEntities> = emptyList(),
    onDismiss: () -> Unit,
    onMemberAdded: (String, List<String>) -> Unit,
) {
    LazyColumn {
        items(contacts) {
            ContactMemberItem(user = it)
        }
    }
}

@Composable
private fun ContactMemberItem(
    user: UserEntities,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = user.name)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun ContactMemberBottomSheetPreview() {
    FriendsSecretsTheme {
        ContactMemberContent(
            sheetState = rememberModalBottomSheetState(),
            onDismiss = {},
            onMemberAdded = { _, _ ->

            }
        )
    }
}