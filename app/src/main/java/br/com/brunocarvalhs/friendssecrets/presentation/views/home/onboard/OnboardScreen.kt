package br.com.brunocarvalhs.friendssecrets.presentation.views.home.onboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

@Composable
fun OnboardingScreen(
    navController: NavHostController,
) {

}

@Composable
private fun OnboardingContent(
    navController: NavHostController,
) {
}

@Composable
@Preview
private fun OnboardingContentPreview() {
    FriendsSecretsTheme {
        OnboardingContent(
            navController = rememberNavController()
        )
    }
}