package br.com.brunocarvalhs.group.details.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.details.R

@Composable
internal fun SuggestedLikesRow(
    alreadyAdded: List<String>,
    onPick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val suggestions = listOf(
        stringResource(R.string.suggested_like_tiktok),
        stringResource(R.string.suggested_like_books),
        stringResource(R.string.suggested_like_games),
        stringResource(R.string.suggested_like_music),
        stringResource(R.string.suggested_like_movies),
        stringResource(R.string.suggested_like_cooking),
        stringResource(R.string.suggested_like_travel),
        stringResource(R.string.suggested_like_tech),
    ).filter { suggestion -> alreadyAdded.none { it.equals(suggestion, ignoreCase = true) } }

    if (suggestions.isEmpty()) return

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(suggestions) { suggestion ->
            AssistChip(onClick = { onPick(suggestion) }, label = { Text(suggestion) })
        }
    }
}
