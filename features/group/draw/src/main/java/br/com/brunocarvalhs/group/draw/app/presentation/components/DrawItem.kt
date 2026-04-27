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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.group.draw.R

@Composable
internal fun DrawItem(
    members: List<UserModel>,
    member: String,
    onShare: () -> Unit
) {
    val drawMember = remember(member, members) {
        members.find { it.name == member }
    }

    val shareDescription = stringResource(R.string.share)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                role = Role.Button
                onClick(label = shareDescription) {
                    onShare()
                    true
                }
            }
            .clickable { onShare() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContactAvatar(
            name = drawMember?.name ?: member,
            photoUrl = drawMember?.photoUrl,
            modifier = Modifier.clearAndSetSemantics { }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = drawMember?.name ?: member,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )

        IconButton(
            onClick = onShare,
            modifier = Modifier.clearAndSetSemantics { }
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun DrawItemPreview() {
    MaterialTheme {
        DrawItem(
            members = listOf(UserModel(name = "Bruno")),
            member = "Bruno",
            onShare = {}
        )
    }
}
