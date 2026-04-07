package br.com.brunocarvalhs.group.draw.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel

@Composable
fun DrawItem(
    members: List<UserModel>,
    member: String,
    onShared: () -> Unit
) {
    val draw = remember { members.find { it.name == member } }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onShared() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContactAvatar(
            name = draw?.name ?: member,
            photoUrl = draw?.photoUrl
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = draw?.name ?: member,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )

        IconButton(onClick = onShared) {
            Icon(Icons.Default.Share, contentDescription = null)
        }
    }
}

@Preview
@Composable
fun ContactListItemPreview() {
    DrawItem(
        members = listOf(),
        member = "Bruno",
        onShared = {}
    )
}
