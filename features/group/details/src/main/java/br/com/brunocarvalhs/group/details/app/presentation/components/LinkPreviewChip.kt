package br.com.brunocarvalhs.group.details.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.details.app.data.services.LinkMetadata
import br.com.brunocarvalhs.group.details.app.data.services.LinkMetadataFetcher
import coil.compose.AsyncImage
import java.net.URI

@Composable
internal fun LinkPreviewChip(url: String) {
    val fetcher = remember { LinkMetadataFetcher() }
    var metadata by remember(url) { mutableStateOf<LinkMetadata?>(null) }
    var isLoading by remember(url) { mutableStateOf(true) }
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(url) {
        metadata = fetcher.fetch(url)
        isLoading = false
    }

    val host = remember(url) { runCatching { URI(url).host }.getOrNull() ?: url }

    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 1.dp,
        modifier = Modifier
            .widthIn(max = 220.dp)
            .clickable { runCatching { uriHandler.openUri(url) } }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            val imageUrl = metadata?.imageUrl
            if (!imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Link,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp).padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text(
                        text = metadata?.title ?: host,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = host,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
