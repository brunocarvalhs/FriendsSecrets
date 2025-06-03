package br.com.brunocarvalhs.auth.app.login

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.auth.R
import br.com.brunocarvalhs.auth.commons.navigation.PhoneSendScreenRoute
import br.com.brunocarvalhs.auth.commons.performance.LaunchPerformanceLifecycleTracing
import br.com.brunocarvalhs.friendssecrets.common.extensions.openUrl
import br.com.brunocarvalhs.friendssecrets.common.navigation.GroupGraphRoute
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme

@Composable
internal fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel
) {
    LaunchPerformanceLifecycleTracing("login")
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (viewModel.session.isUserLoggedIn()) {
            navController.navigate(GroupGraphRoute)
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            LoginUiState.PrivacyPolicy -> context.openUrl(url = "https://github.com/brunocarvalhs/FriendsSecrets/blob/develop/docs/PrivacyPolicy.md")
            LoginUiState.TermsOfUse -> context.openUrl(url = "https://github.com/brunocarvalhs/FriendsSecrets/blob/develop/docs/TermsEndConditions.md")
            LoginUiState.Register -> navController.navigate(PhoneSendScreenRoute)
            LoginUiState.AcceptNotRegister -> {
                viewModel.session.setUserAnonymous()
                navController.navigate(GroupGraphRoute)
            }
            else -> {}
        }
    }

    if (uiState == LoginUiState.None) return

    LoginContent(
        handleIntent = viewModel::handleIntent
    )
}

@Composable
private fun LoginContent(
    handleIntent: (LoginIntent) -> Unit = {},
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_logo_friends_secrets),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(
                    R.string.login_screen_welcome,
                    stringResource(R.string.app_name)
                ),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.login_screen_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            val linkColor = MaterialTheme.colorScheme.primary
            val bodyStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)

            val annotatedString = buildAnnotatedString {
                pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
                withStyle(SpanStyle(color = linkColor, fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.login_screen_privacy))
                }
                pop()

                append(stringResource(R.string.login_screen_and))

                pushStringAnnotation(tag = "TERMS", annotation = "terms")
                withStyle(SpanStyle(color = linkColor, fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.login_screen_terms))
                }
                pop()
                append(".")
            }

            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(offset, offset).firstOrNull()
                        ?.let { annotation ->
                            when (annotation.tag) {
                                "PRIVACY" -> handleIntent(LoginIntent.PrivacyPolicy)
                                "TERMS" -> handleIntent(LoginIntent.TermsOfUse)
                            }
                        }
                },
                modifier = Modifier.padding(top = 8.dp),
                style = bodyStyle
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { handleIntent(LoginIntent.Accept) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(stringResource(R.string.login_screen_button_register))
            }

            TextButton(
                onClick = { handleIntent(LoginIntent.AcceptNotRegister) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                Text(stringResource(R.string.login_screen_button_anonymation))
            }
        }
    }
}


@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
private fun LoginContentPreview() {
    FriendsSecretsTheme {
        LoginContent()
    }
}
