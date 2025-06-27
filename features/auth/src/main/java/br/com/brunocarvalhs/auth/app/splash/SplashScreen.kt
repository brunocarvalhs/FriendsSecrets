@file:Suppress("IMPLICIT_CAST_TO_ANY")

package br.com.brunocarvalhs.auth.app.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.auth.commons.navigation.BiometricScreenRoute
import br.com.brunocarvalhs.auth.commons.navigation.LoginScreenRoute
import br.com.brunocarvalhs.friendssecrets.common.navigation.GroupGraphRoute
import br.com.brunocarvalhs.friendssecrets.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme

@Composable
internal fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
) {
    val state by viewModel.state.collectAsState()
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(state) {
        alphaAnim.animateTo(
            targetValue = 1f, animationSpec = tween(durationMillis = 1000)
        )
        val route = when (state) {
            is SplashUiState.Biometric -> BiometricScreenRoute
            is SplashUiState.Success -> GroupGraphRoute
            else -> LoginScreenRoute
        }
        navController.navigate(route) {
            popUpTo(0)
        }
    }

    SplashScreenContent()
}

@Composable
private fun SplashScreenContent(
    modifier: Modifier = Modifier,
) {
    Scaffold {
        LoadingProgress(modifier = modifier.padding(it))
    }
}


@Composable
@Preview
private fun SplashScreenPreview() {
    FriendsSecretsTheme {
        SplashScreenContent()
    }
}
