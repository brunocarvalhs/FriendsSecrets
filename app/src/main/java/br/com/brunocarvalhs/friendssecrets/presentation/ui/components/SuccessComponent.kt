package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun SuccessComponent(modifier: Modifier = Modifier, redirectTo: () -> Unit = {}) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
    )

    val isAnimationFinished = progress == 1f

    val backgroundColor = if (isAnimationFinished) Color(0xFF00CA8D)
    else MaterialTheme.colorScheme.onBackground

    LaunchedEffect(isAnimationFinished) {
        delay(timeMillis = 500)
        if (isAnimationFinished) {
            redirectTo()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier.size(200.dp),
            composition = composition,
            progress = progress
        )
        if (isAnimationFinished) {
            Text(
                text = "Success!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SuccessComponentPreview() {
    FriendsSecretsTheme {
        SuccessComponent()
    }
}
