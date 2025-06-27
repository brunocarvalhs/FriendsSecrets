package br.com.brunocarvalhs.auth.app.phoneVerify

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.auth.R
import br.com.brunocarvalhs.auth.commons.navigation.CreateProfileScreenRoute
import br.com.brunocarvalhs.auth.commons.performance.LaunchPerformanceLifecycleTracing
import br.com.brunocarvalhs.friendssecrets.common.extensions.toMaskedPhoneNumber
import br.com.brunocarvalhs.friendssecrets.ui.components.LoadingComponent
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import kotlinx.coroutines.delay

@Composable
fun PhoneVerifyScreen(
    activity: ComponentActivity? = null,
    phoneNumber: String? = null,
    countryCode: String? = null,
    navController: NavController,
    viewModel: PhoneVerifyViewModel
) {
    LaunchPerformanceLifecycleTracing("phone_verify")
    val uiState by viewModel.uiState.collectAsState()

    phoneNumber ?: run {
        navController.popBackStack()
        return
    }

    LaunchedEffect(uiState) {
        if (uiState is PhoneVerifyUiState.Success) {
            navController.navigate(CreateProfileScreenRoute) {
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    PhoneVerifyContent(
        navController = navController,
        uiState = uiState,
        activity = activity,
        phoneNumber = phoneNumber,
        countryCode = countryCode.orEmpty(),
        handleIntent = viewModel::handleIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneVerifyContent(
    navController: NavController = rememberNavController(),
    activity: ComponentActivity? = null,
    uiState: PhoneVerifyUiState = PhoneVerifyUiState.Idle,
    phoneNumber: String = "",
    countryCode: String = "",
    handleIntent: (PhoneVerifyIntent) -> Unit = {}
) {
    val otpLength = 6
    var otpCode by remember { mutableStateOf(List(otpLength) { "" }) }
    var remainingTime by remember { mutableIntStateOf(60) }
    var showError by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime--
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    NavigationBackIconButton(navController = navController)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
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
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = stringResource(
                        R.string.code_has_been_send_to,
                        phoneNumber.toMaskedPhoneNumber()
                    ),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    otpCode.forEachIndexed { index, char ->
                        OutlinedTextField(
                            value = char,
                            onValueChange = { input ->
                                if (input.length <= 1 && (input.isEmpty() || input.all { it.isDigit() })) {
                                    val newOtp = otpCode.toMutableList()
                                    newOtp[index] = input
                                    otpCode = newOtp

                                    if (input.isNotEmpty() && index < otpLength - 1) {
                                        focusRequesters[index + 1].requestFocus()
                                    } else if (input.isEmpty() && index > 0) {
                                        focusRequesters[index - 1].requestFocus()
                                    } else {
                                        focusManager.clearFocus()
                                    }

                                    showError = false
                                }
                            },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            isError = showError,
                            modifier = Modifier
                                .width(48.dp)
                                .height(64.dp)
                                .focusRequester(focusRequesters[index]),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                if (showError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.invalid_code),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (remainingTime > 0) {
                    Text(
                        text = stringResource(R.string.resend_code_in_s, remainingTime),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.resend_code_now),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            remainingTime = 60
                            activity?.let {
                                handleIntent(
                                    PhoneVerifyIntent.ResendCode(
                                        activity = activity,
                                        phone = phoneNumber,
                                        countryCode = countryCode
                                    )
                                )
                            }
                        }
                    )
                }
            }

            Button(
                onClick = {
                    if (otpCode.any { it.isBlank() }) {
                        showError = true
                        return@Button
                    }

                    handleIntent(PhoneVerifyIntent.VerifyCode(otpCode.joinToString("")))
                },
                enabled = otpCode.all { it.isNotBlank() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(stringResource(R.string.verify))
            }
        }
    }

    if (uiState is PhoneVerifyUiState.Loading) {
        LoadingComponent()
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
private fun PhoneVerifyScreenPreview() {
    FriendsSecretsTheme {
        PhoneVerifyContent()
    }
}