package br.com.brunocarvalhs.friendssecrets.presentation.views.home.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.extensions.isFistAppOpen
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.HomeNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.login.components.EmailSignInButton
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.login.components.FacebookSignInButton
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.login.components.GoogleSignInButton
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.login.components.PhoneSignInButton
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val context = LocalContext.current
    val uiState: LoginUiState by viewModel.uiState.collectAsState()

    val launcher =
        rememberLauncherForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.redirect {
                    navController.navigate(HomeNavigation.Home.route)
                }
            }
        }

    LaunchedEffect(Unit) {
        if (context.isFistAppOpen()) navController.navigate(HomeNavigation.Onboarding.route)
    }

    LoginContent(
        launcher = launcher,
        state = uiState,
        onLogin = viewModel::onEvent
    )
}

@Composable
private fun LoginContent(
    launcher: ActivityResultLauncher<Intent>? = null,
    state: LoginUiState,
    onLogin: (LoginIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Image Section
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(3f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo__friends_secrets),
                contentDescription = "Top Illustration",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )
        }

        // Text Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Organize seus amigos secretos\nem um lugar",
                color = Color.Red,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailSignInButton(
                enabled = state != LoginUiState.Loading
            ) {
                launcher?.let {
                    onLogin.invoke(LoginIntent.EmailAuth(launcher))
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            PhoneSignInButton(
                enabled = state != LoginUiState.Loading
            ) {
                launcher?.let {
                    onLogin.invoke(LoginIntent.PhoneAuth(launcher))
                }
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            GoogleSignInButton(
                enabled = state != LoginUiState.Loading,
                isCompact = true
            ) {
                launcher?.let {
                    onLogin.invoke(LoginIntent.GoogleAuth(launcher))
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            FacebookSignInButton(
                enabled = state != LoginUiState.Loading,
                isCompact = true
            ) {
                launcher?.let {
                    onLogin.invoke(LoginIntent.FacebookAuth(launcher))
                }
            }
        }
    }
}

@Composable
@Preview
private fun PreviewLoginScreen() {
    LoginContent(
        state = LoginUiState.Idle
    ) {

    }
}