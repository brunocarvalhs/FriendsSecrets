package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.login

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.R

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.Factory
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            LoginUiState.PrivacyPolicy -> {
                navController.navigate("privacy_policy")
            }

            LoginUiState.TermsOfUse -> {
                navController.navigate("terms_of_use")
            }

            LoginUiState.Register -> {
                navController.navigate("phone_send")
            }

            else -> {}
        }
    }

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
                painter = painterResource(id = R.drawable.ic_logo__friends_secrets),
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Bem-vindo ao ${stringResource(R.string.app_name)}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Leia nossa Política de Privacidade. Toque em 'Aceitar e Continuar' para aceitar os Termos de Serviço.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            val annotatedString = buildAnnotatedString {
                append("Leia nossa ")
                pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
                withStyle(SpanStyle(color = Color(0xFF128C7E), fontWeight = FontWeight.Bold)) {
                    append("Política de Privacidade")
                }
                pop()

                append(" e ")

                pushStringAnnotation(tag = "TERMS", annotation = "terms")
                withStyle(SpanStyle(color = Color(0xFF128C7E), fontWeight = FontWeight.Bold)) {
                    append("Termos de Serviço")
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
                style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { handleIntent(LoginIntent.Accept) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("Aceitar e Continuar")
            }
        }
    }
}


@Preview
@Composable
private fun LoginContentPreview() {
    LoginContent()
}
