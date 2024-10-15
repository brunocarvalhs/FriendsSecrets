package br.com.brunocarvalhs.friendssecrets.presentation.views.home.biometric

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.HomeNavigation
import timber.log.Timber

@Composable
fun BiometricScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current as FragmentActivity
    val executor = remember { ContextCompat.getMainExecutor(context) }
    val biometricPrompt =
        BiometricPrompt(context, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Timber.e("onAuthenticationError: $errorCode : $errString")
            }

            @RequiresApi(Build.VERSION_CODES.R)
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                navController.navigate(HomeNavigation.Home.route)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Timber.e("onAuthenticationFailed")
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setAllowedAuthenticators(BIOMETRIC_STRONG)
        .setTitle("Biometric Authentication")
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("Use account password")
        .build()

    LaunchedEffect(Unit) {
        biometricPrompt.authenticate(promptInfo)
    }

    BiometricContent {
        biometricPrompt.authenticate(promptInfo)
    }
}

@Composable
private fun BiometricContent(
    onClick: () -> Unit,
) {
    Scaffold {
        Column(
            modifier = Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(Icons.Filled.Lock, contentDescription = null)
                Text(text = "Biometric Authentication")
            }
            TextButton(
                onClick = onClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Desbloquear")
            }
        }
    }
}

@Composable
@Preview
private fun BiometricContentPreview() {
    FriendsSecretsTheme {
        BiometricContent(
            onClick = {

            }
        )
    }
}