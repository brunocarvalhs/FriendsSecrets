package br.com.brunocarvalhs.group.details.app.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.R
import coil.compose.AsyncImage

@Composable
internal fun ContactItem(
    name: String = "",
    subtitle: String? = null,
    photoUrl: String? = null,
    likes: List<String> = emptyList(),
    isSelected: Boolean = false,
    isLikedExpanded: Boolean = false,
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
    onSuggestGifts: (() -> Unit)? = null,
    action: @Composable ((HashMap<String?, String?>, Boolean) -> Unit)? = null,
) {
    val filteredLikes = remember(likes) {
        likes.filter { it.isNotBlank() }
    }

    val expandedState = stringResource(R.string.likes_expanded_state)
    val collapsedState = stringResource(R.string.likes_collapsed_state)


    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    var isLiked by remember { mutableStateOf(isLikedExpanded) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .clickable(
                enabled = filteredLikes.isNotEmpty(),
                onClickLabel = stringResource(
                    if (isLiked) R.string.collapse_likes_action else R.string.expand_likes_action
                ),
                role = Role.Button,
                onClick = {
                    isLiked = !isLiked
                }
            )
            .semantics(mergeDescendants = true) {
                stateDescription = if (isLiked) expandedState
                else collapsedState
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
                if (!photoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = stringResource(
                            R.string.contact_photo_description,
                            name
                        ),
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.1f)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name.take(1).uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    action?.invoke(
                        hashMapOf(GroupModel.NAME to name, GroupModel.PHOTO to photoUrl),
                        isLiked
                    )
                }
            }

            if (filteredLikes.isNotEmpty()) {
                AnimatedVisibility(visible = isLiked) {
                    Column {
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

                        if (onSuggestGifts != null) {
                            TextButton(
                                onClick = onSuggestGifts,
                                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                            ) {
                                Text(stringResource(R.string.suggest_gifts_action))
                            }
                        }
                    }
                }
            }
        }
    }
}
