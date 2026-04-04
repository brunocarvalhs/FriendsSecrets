package br.com.brunocarvalhs.group.create.app.presentation.contacts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun ContactAvatar(
    name: String,
    photoUrl: String?,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 48.dp
) {
    val initials = remember(name) {
        val sanitized = name.filter { it.isLetter() || it.isWhitespace() }
        val words = sanitized.split(" ").filter { it.isNotBlank() }

        when {
            words.size >= 2 -> {
                words.take(2)
                    .map { it.first().uppercase() }
                    .joinToString("")
            }
            words.size == 1 -> {
                words.first().take(2).uppercase()
            }
            else -> "?"
        }
    }

    SubcomposeAsyncImage(
        model = photoUrl,
        contentDescription = null,
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        error = {
            AvatarFallback(initials = initials, size = size)
        },
        loading = {
            AvatarFallback(initials = initials, size = size)
        }
    )
}

@Composable
private fun AvatarFallback(
    initials: String,
    size: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = if (size < 50.dp) MaterialTheme.typography.bodySmall else MaterialTheme.typography.titleMedium
        )
    }
}
