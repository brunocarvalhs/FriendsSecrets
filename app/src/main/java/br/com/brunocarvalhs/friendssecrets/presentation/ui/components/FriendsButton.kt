package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.LocalDimensions

/**
 * Componente de botão padronizado para uso em toda a aplicação.
 * Isso garante consistência visual e facilita a manutenção.
 */
@Composable
fun FriendsButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isFullWidth: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = LocalDimensions.current.spacingMedium,
        vertical = LocalDimensions.current.spacingSmall
    )
) {
    val buttonModifier = if (isFullWidth) {
        modifier
            .fillMaxWidth()
            .height(LocalDimensions.current.buttonHeight)
    } else {
        modifier.height(LocalDimensions.current.buttonHeight)
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = contentPadding,
        modifier = buttonModifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Componente de botão outline padronizado para uso em toda a aplicação.
 */
@Composable
fun FriendsOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isFullWidth: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = LocalDimensions.current.spacingMedium,
        vertical = LocalDimensions.current.spacingSmall
    )
) {
    val buttonModifier = if (isFullWidth) {
        modifier
            .fillMaxWidth()
            .height(LocalDimensions.current.buttonHeight)
    } else {
        modifier.height(LocalDimensions.current.buttonHeight)
    }

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = contentPadding,
        modifier = buttonModifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendsButtonPreview() {
    FriendsSecretsTheme {
        FriendsButton(
            text = "Botão Primário",
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendsOutlinedButtonPreview() {
    FriendsSecretsTheme {
        FriendsOutlinedButton(
            text = "Botão Secundário",
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendsButtonDisabledPreview() {
    FriendsSecretsTheme {
        FriendsButton(
            text = "Botão Desabilitado",
            onClick = {},
            enabled = false,
            modifier = Modifier.padding(16.dp)
        )
    }
}