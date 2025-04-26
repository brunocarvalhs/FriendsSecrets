package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.LocalDimensions

/**
 * Componente de card padronizado para uso em toda a aplicação.
 * Isso garante consistência visual e facilita a manutenção.
 */
@Composable
fun FriendsCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    border: BorderStroke? = null,
    elevation: Dp = LocalDimensions.current.elevationSmall,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = border,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
    ) {
        Column(
            modifier = Modifier.padding(LocalDimensions.current.spacingMedium)
        ) {
            content()
        }
    }
}

/**
 * Componente de card outline padronizado para uso em toda a aplicação.
 */
@Composable
fun FriendsOutlinedCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    borderWidth: Dp = LocalDimensions.current.borderSmall,
    content: @Composable ColumnScope.() -> Unit
) {
    FriendsCard(
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        border = BorderStroke(borderWidth, borderColor),
        elevation = 0.dp,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun FriendsCardPreview() {
    FriendsSecretsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            FriendsCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Card com Elevação",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Este é um exemplo de card com elevação para uso em toda a aplicação.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendsOutlinedCardPreview() {
    FriendsSecretsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            FriendsOutlinedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Card com Borda",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Este é um exemplo de card com borda para uso em toda a aplicação.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}