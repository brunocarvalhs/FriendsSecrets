package br.com.brunocarvalhs.group.create.app.presentation.contacts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel
import coil.compose.SubcomposeAsyncImage

@Composable
fun ContactListItem(
    contact: ContactModel,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    val initials = remember(contact.name) {
        val sanitized = contact.name.filter { it.isLetter() || it.isWhitespace() }
        val words = sanitized.split(" ").filter { it.isNotBlank() }

        when {
            words.size >= 2 -> {
                words.take(2).joinToString("") { it.first().uppercase() }
            }

            words.size == 1 -> {
                words.first().take(2).uppercase()
            }

            else -> "?"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            model = contact.photoUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            error = {
                AvatarFallback(initials = initials)
            },
            loading = {
                AvatarFallback(initials = initials)
            }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = contact.name,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )

        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary,
                uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                checkmarkColor = MaterialTheme.colorScheme.onSecondary
            )
        )
    }
}

@Composable
private fun AvatarFallback(initials: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
fun ContactListItemPreview() {
    ContactListItem(
        contact = ContactModel(
            id = "1",
            name = "Bruno Carvalho",
            phoneNumber = "1234567890"
        ),
        isSelected = true,
        onToggle = {}
    )
}
