package br.com.brunocarvalhs.group.create.app.presentation.forms.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel
import br.com.brunocarvalhs.group.create.app.presentation.contacts.components.ContactAvatar

@Composable
fun MemberAvatarItem(
    member: ContactModel,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.width(64.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            ContactAvatar(
                name = member.name,
                photoUrl = member.photoUrl,
                size = 56.dp
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = member.name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun MemberAvatarItemPreview() {
    MemberAvatarItem(
        member = ContactModel(
            id = "1",
            name = "John Doe",
            phoneNumber = "1234567890"
        ),
    )
}
