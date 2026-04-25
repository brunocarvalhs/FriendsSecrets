package br.com.brunocarvalhs.group.draw.app.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.draw.R

@Composable
internal fun DrawActionButton(
    isFalling: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.Companion
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.extraLarge,
        enabled = !isFalling
    ) {
        Text(
            text = if (isFalling) {
                stringResource(R.string.drawing)
            } else {
                stringResource(R.string.just_drawn)
            },
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Companion.Bold)
        )
    }
}
