package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneSend

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.LoginNavigation

@Composable
fun PhoneSendScreen(
    navController: NavController,
    viewModel: PhoneSendViewModel = viewModel(
        factory = PhoneSendViewModel.Factory()
    ),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is PhoneSendUiState.Error -> {

            }

            is PhoneSendUiState.Success -> {
                navController.navigate(
                    route = LoginNavigation.PhoneVerification.createRoute(
                        phoneNumber = state.phone
                    )
                )
            }

            else -> {}
        }
    }

    PhoneSendContent(
        navController = navController,
        handleIntent = viewModel::handleIntent,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneSendContent(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    uiState: PhoneSendUiState = PhoneSendUiState.Idle,
    handleIntent: (PhoneSendIntent) -> Unit = {},
) {
    var phoneNumber by remember { mutableStateOf("") }

    val countryCode = "+55"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    NavigationBackIconButton(navController = navController)
                }
            )
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Enter your phone number",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "WhatsApp will need to verify your phone number.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            ClickableText(
                text = AnnotatedString("What’s my number?"),
                onClick = {

                },
                style = TextStyle(
                    color = Color.Blue,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RectangleShape)
                    .padding(8.dp)
            ) {
                Text(
                    text = countryCode,
                    modifier = Modifier.padding(end = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                HorizontalDivider(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    placeholder = { Text("Phone number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Carrier charges may apply",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    handleIntent(
                        PhoneSendIntent.SendCode(
                            phone = phoneNumber,
                            countryCode = countryCode
                        )
                    )
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("NEXT")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (uiState is PhoneSendUiState.Loading) {
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
}

@Preview(showBackground = true)
@Composable
private fun PhoneSendScreenPreview() {
    PhoneSendContent()
}