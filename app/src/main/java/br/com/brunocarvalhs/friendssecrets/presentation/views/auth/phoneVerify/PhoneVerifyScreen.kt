package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneVerify

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.LoginNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneSend.PhoneSendUiState
import kotlinx.coroutines.delay

@Composable
fun PhoneVerifyScreen(
    phoneNumber: String? = null,
    navController: NavController,
    viewModel: PhoneVerifyViewModel = viewModel(
        factory = PhoneVerifyViewModel.Factory
    ),
) {
    val uiState by viewModel.uiState.collectAsState()

    phoneNumber ?: run {
        navController.popBackStack()
        return
    }

    LaunchedEffect(uiState) {
        if (uiState is PhoneVerifyUiState.Success) {
            navController.navigate(LoginNavigation.Profile.route)
        }
    }

    PhoneVerifyContent(
        uiState = uiState,
        phoneNumber = phoneNumber,
        handleIntent = viewModel::handleIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneVerifyContent(
    uiState: PhoneVerifyUiState = PhoneVerifyUiState.Idle,
    phoneNumber: String = "",
    handleIntent: (PhoneVerifyIntent) -> Unit = {}
) {
    val otpLength = 6
    var otpCode by remember { mutableStateOf(List(otpLength) { "" }) }
    var remainingTime by remember { mutableIntStateOf(56) }

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime--
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {
                            // Voltar
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Code has been send to ${phoneNumber.toMaskedPhoneNumber()}",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(32.dp))

                val focusManager = LocalFocusManager.current
                val focusRequesters = remember { List(otpLength) { FocusRequester() } }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    otpCode.forEachIndexed { index, char ->
                        OutlinedTextField(
                            value = char,
                            onValueChange = { input ->
                                if (input.length <= 1 && (input.isEmpty() || input.all { it.isDigit() })) {
                                    otpCode = otpCode.toMutableList().also { list ->
                                        list[index] = input
                                    }

                                    when {
                                        input.isNotEmpty() && index < otpLength - 1 -> {
                                            focusRequesters[index + 1].requestFocus()
                                        }

                                        input.isEmpty() && index > 0 -> {
                                            focusRequesters[index - 1].requestFocus()
                                        }

                                        input.isEmpty() -> {
                                            focusRequesters[index].requestFocus()
                                        }

                                        else -> {
                                            focusManager.clearFocus()
                                        }
                                    }
                                }
                            },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .width(48.dp)
                                .height(68.dp)
                                .focusRequester(focusRequesters[index]),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Resend Code in ${remainingTime}s",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color(0xFF009688)
                )
            }

            Button(
                onClick = {
                    handleIntent(
                        PhoneVerifyIntent.VerifyCode(
                            code = otpCode.joinToString("")
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Verify")
            }
        }
    }
    if (uiState is PhoneVerifyUiState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x88000000)) // fundo escurecido
                .clickable(enabled = false) {}, // bloqueia cliques
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.size(100.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

fun String.toMaskedPhoneNumber(): String {
    return if (this.length >= 10) {
        val areaCode = this.substring(2, 4)
        val masked = "******"
        val lastTwo = this.takeLast(2)
        "$areaCode$masked$lastTwo"
    } else {
        this
    }
}


@Preview(showBackground = true)
@Composable
private fun PhoneVerifyScreenPreview() {
    PhoneVerifyContent()
}