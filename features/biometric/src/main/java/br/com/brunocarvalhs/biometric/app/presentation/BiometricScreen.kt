package br.com.brunocarvalhs.biometric.app.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import br.com.brunocarvalhs.biometric.R

@Composable
internal fun BiometricScreen(
    state: BiometricUiState,
    handleIntent: (BiometricIntent) -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current

    BiometricEffects(handleIntent, state.isAuthenticated, onSuccess)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BiometricContent(
            state = state,
            onAuthenticate = {
                val activity = context as? FragmentActivity
                activity?.let {
                    handleIntent(BiometricIntent.Authenticate(it))
                }
            }
        )
    }
}

@Composable
private fun BiometricEffects(
    handleIntent: (BiometricIntent) -> Unit,
    isAuthenticated: Boolean,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val currentHandleIntent by rememberUpdatedState(handleIntent)
    val currentOnSuccess by rememberUpdatedState(onSuccess)

    LaunchedEffect(Unit) {
        val activity = context as? FragmentActivity
        activity?.let {
            currentHandleIntent(BiometricIntent.Authenticate(it))
        }
    }

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            currentOnSuccess()
        }
    }
}

@Composable
private fun BiometricContent(
    state: BiometricUiState,
    onAuthenticate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BiometricHeader()

        Spacer(modifier = Modifier.height(32.dp))

        BiometricDescription()

        state.error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        BiometricButton(
            isLoading = state.isLoading,
            onClick = onAuthenticate
        )
    }
}

@Composable
private fun BiometricHeader() {
    Icon(
        imageVector = Icons.Default.Fingerprint,
        contentDescription = null,
        modifier = Modifier.size(120.dp),
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun BiometricDescription() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.biometric_required),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.biometric_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BiometricButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(stringResource(R.string.biometric_button_authentic))
        }
    }
}
