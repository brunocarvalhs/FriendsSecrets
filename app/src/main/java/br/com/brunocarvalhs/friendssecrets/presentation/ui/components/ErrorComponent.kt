package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay


@Composable
fun ErrorComponent(
    modifier: Modifier = Modifier,
    message: String,
    onRefresh: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier.size(200.dp),
            composition = composition,
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ops!",
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(Modifier.size(32.dp))
            onRefresh?.let { refresh ->
                Button(
                    onClick = { refresh.invoke() }
                ) {
                    Text(text = "Tenta novamente")
                }
            }
            onBack?.let { back ->
                Spacer(Modifier.size(8.dp))
                if (onRefresh == null) {
                    Button(
                        onClick = { back.invoke() }
                    ) {
                        Text(text = "Ir para home")
                    }
                } else {
                    TextButton(
                        onClick = { back.invoke() }
                    ) {
                        Text(text = "Ir para home")
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ErrorComponentPreview() {
    FriendsSecretsTheme {
        ErrorComponent(message = "Estamos com problema no momento")
    }
}

@Composable
@Preview(showBackground = true)
private fun ErrorComponentButtonOnRefreshPreview() {
    FriendsSecretsTheme {
        ErrorComponent(message = "Estamos com problema no momento", onRefresh = {})
    }
}

@Composable
@Preview(showBackground = true)
private fun ErrorComponentButtonOnBackPreview() {
    FriendsSecretsTheme {
        ErrorComponent(message = "Estamos com problema no momento", onBack = {})
    }
}

@Composable
@Preview(showBackground = true)
private fun ErrorComponentButtonAllButtonPreview() {
    FriendsSecretsTheme {
        ErrorComponent(message = "Estamos com problema no momento", onBack = {}, onRefresh = {})
    }
}
