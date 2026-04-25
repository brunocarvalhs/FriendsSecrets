package br.com.brunocarvalhs.group.draw.app.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.draw.R

@Composable
internal fun DrawHeader(modifier: Modifier = Modifier.Companion) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.ready_to_draw),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Companion.Bold,
                textAlign = TextAlign.Companion.Center
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.Companion.height(8.dp))

        Text(
            text = stringResource(R.string.tap_the_button_below_to_start_the_draw),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Companion.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
