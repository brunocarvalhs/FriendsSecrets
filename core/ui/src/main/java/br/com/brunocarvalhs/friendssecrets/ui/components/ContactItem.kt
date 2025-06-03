package br.com.brunocarvalhs.friendssecrets.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.ui.R
import br.com.brunocarvalhs.friendssecrets.ui.fake.toFake
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import coil.compose.AsyncImage

@Composable
fun ContactItem(
    contact: UserEntities,
    isSelected: Boolean = false,
    isLikedExpanded: Boolean = false,
    action: @Composable ((UserEntities, Boolean) -> Unit)? = null,
) {
    val context = LocalContext.current
    val filteredLikes = remember(contact.likes) {
        contact.likes.filter { it.isNotBlank() }
    }

    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    var isLiked by remember { mutableStateOf(isLikedExpanded) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(
                enabled = filteredLikes.isNotEmpty(),
                onClickLabel = stringResource(if (isLiked) R.string.collapse_likes_action else R.string.expand_likes_action),
                role = Role.Button,
                onClick = {
                    isLiked = !isLiked
                }
            )
            .semantics(mergeDescendants = true) {
                stateDescription = if (isLiked)
                    context.getString(R.string.likes_expanded_state)
                else
                    context.getString(R.string.likes_collapsed_state)
            },
        tonalElevation = if (isSelected) 4.dp else 2.dp,
        color = backgroundColor,
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!contact.photoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = contact.photoUrl,
                        contentDescription = stringResource(
                            R.string.contact_photo_description,
                            contact.name
                        ),
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.1f)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Default user icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                    if (contact.phoneNumber.isNotBlank()) {
                        Text(
                            text = contact.phoneNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    action?.invoke(contact, isLiked)
                }
            }

            if (filteredLikes.isNotEmpty()) {
                AnimatedVisibility(visible = isLiked) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredLikes) { like ->
                            if (like.isNotBlank()) {
                                AssistChip(onClick = {}, label = { Text(like) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
private fun ContactItemPreview() {
    ContactItem(
        contact = UserEntities.toFake(
            name = "Produto de Teste",
            id = "1",
            phoneNumber = "123456789",
            photoUrl = "",
            isPhoneNumberVerified = false,
            likes = listOf("Like 1", "Like 2", "Like 3")
        ),
        isLikedExpanded = true,
    )
}

@Composable
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
private fun ContactItemSelectedPreview() {
    FriendsSecretsTheme {
        ContactItem(
            contact = UserEntities.toFake(
                name = "Produto de Teste",
                id = "1",
                phoneNumber = "123456789",
                photoUrl = "",
                isPhoneNumberVerified = false,
                likes = listOf("Like 1", "Like 2", "Like 3")
            ),
            isSelected = true,
            isLikedExpanded = true,
        )
    }
}
