package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneSend

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
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
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.LoginNavigation
import com.arpitkatiyarprojects.countrypicker.CountryPickerOutlinedTextField
import com.arpitkatiyarprojects.countrypicker.models.CountriesListDialogDisplayProperties
import com.arpitkatiyarprojects.countrypicker.models.CountryDetails
import com.arpitkatiyarprojects.countrypicker.models.SelectedCountryDisplayProperties
import com.arpitkatiyarprojects.countrypicker.utils.CountryPickerUtils

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
    val keyboardController = LocalSoftwareKeyboardController.current

    var phoneNumber by remember { mutableStateOf("") }

    val selectedCountryDisplayProperties by remember {
        mutableStateOf(SelectedCountryDisplayProperties())
    }

    val countriesListDialogDisplayProperties by remember {
        mutableStateOf(CountriesListDialogDisplayProperties())
    }

    var selectedCountryState by remember {
        mutableStateOf<CountryDetails?>(null)
    }

    var isMobileNumberValidationError by remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        TopAppBar(title = {}, navigationIcon = {
            NavigationBackIconButton(navController = navController)
        })
    }) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(R.string.phone_send_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(
                        R.string.phone_send_description,
                        stringResource(R.string.app_name)
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                CountryPickerOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.phone_send_textfield_label)) },
                    mobileNumber = CountryPickerUtils.getFormattedMobileNumber(
                        phoneNumber, selectedCountryState?.countryCode ?: "IN",
                    ),
                    onMobileNumberChange = {
                        phoneNumber = it
                        isMobileNumberValidationError = !CountryPickerUtils.isMobileNumberValid(
                            phoneNumber,
                            selectedCountryState?.countryCode ?: "IN"
                        )
                    },
                    onCountrySelected = {
                        selectedCountryState = it
                    },
                    selectedCountryDisplayProperties = selectedCountryDisplayProperties,
                    countriesListDialogDisplayProperties = countriesListDialogDisplayProperties,
                    isError = isMobileNumberValidationError,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.phone_send_textfield_description),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Column {
                Button(
                    onClick = {
                        keyboardController?.hide()

                        handleIntent(
                            PhoneSendIntent.SendCode(
                                phone = phoneNumber,
                                countryCode = selectedCountryState?.countryPhoneNumberCode.orEmpty()
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(stringResource(R.string.phone_send_button))
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    if (uiState is PhoneSendUiState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x88000000))
                .clickable(enabled = false) {}, contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.size(100.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(
    name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO
)
@Composable
private fun PhoneSendScreenPreview() {
    FriendsSecretsTheme {
        PhoneSendContent()
    }
}