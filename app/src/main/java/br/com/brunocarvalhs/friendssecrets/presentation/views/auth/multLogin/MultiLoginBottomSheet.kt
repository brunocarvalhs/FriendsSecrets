package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.multLogin

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.EmailSignInButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.GoogleSignInButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.PhoneSignInButton
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiLoginBottomSheet(
    viewModel: MultiLoginViewModel = hiltViewModel(),
    onLoading: () -> Unit = {},
    onDismiss: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    val context = LocalContext.current
    val modalBottomSheetState = rememberModalBottomSheetState()

    val launcher =
        rememberLauncherForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            onLoading()
            if (result.resultCode == RESULT_OK) {
                viewModel.handlerIntent(MultiLoginViewIntent.Success(onLoginSuccess))
            } else {
                val message = result.idpResponse?.error?.message.orEmpty()
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.handlerIntent(MultiLoginViewIntent.Error(message))
            }
        }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        LoginContent(
            handlerIntent = viewModel::handlerIntent,
            launcher = launcher
        )
    }
}

@Composable
private fun LoginContent(
    handlerIntent: (MultiLoginViewIntent) -> Unit = {},
    launcher: ActivityResultLauncher<Intent>? = null,
) {
    Surface(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GoogleSignInButton {
                launcher?.let {
                    handlerIntent(MultiLoginViewIntent.GoogleAuth(it))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            PhoneSignInButton {
                launcher?.let {
                    handlerIntent(MultiLoginViewIntent.PhoneAuth(it))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            EmailSignInButton {
                launcher?.let {
                    handlerIntent(MultiLoginViewIntent.EmailAuth(it))
                }
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
private fun LoginBottomSheetPreview() {
    LoginContent()
}

