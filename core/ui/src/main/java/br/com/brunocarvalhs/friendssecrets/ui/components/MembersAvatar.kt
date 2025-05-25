package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import coil.compose.AsyncImage

@Composable
fun MembersAvatar(
    modifier: Modifier = Modifier,
    members: List<UserEntities> = emptyList(),
    shape: Shape = CircleShape,
    size: Int = 40
) {
    val overlapOffset = (-10).dp
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(overlapOffset)
    ) {
        if (members.size > 3) {
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(1.dp, Color.White, shape)
                    .zIndex(members.size.toFloat()),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+${members.size - 3}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        members.take(3).forEachIndexed { index, member ->
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(1.dp, Color.White, shape)
                    .zIndex((members.size - index - 1).toFloat()),
                contentAlignment = Alignment.Center
            ) {
                if (member.photoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = member.photoUrl,
                        contentDescription = "Member photo",
                        modifier = Modifier
                            .size(size.dp)
                            .clip(shape)
                            .background(Color.Gray.copy(alpha = 0.1f)),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = member.name.first().toString(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
@Preview
private fun MembersAvatarPreview() {
    MembersAvatar(
        members = listOf()
    )
}