package br.com.brunocarvalhs.settings.app.termsAndConditions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.brunocarvalhs.settings.R
import br.com.brunocarvalhs.settings.app.termsAndConditions.TermsAndConditionsViewModel.Companion.DEFAULT_URL
import br.com.brunocarvalhs.settings.commons.components.WebViewContent

@Composable
internal fun TermsAndConditionScreen(
    onBack: () -> Unit,
    viewModel: TermsAndConditionsViewModel = hiltViewModel()
) {
    val url by viewModel.url.collectAsState()

    TermsAndConditionsContent(
        url = url,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TermsAndConditionsContent(
    url: String,
    onBack: () -> Unit
) {
    WebViewContent(
        url = url,
        title = {
            Text(text = stringResource(R.string.title_terms_and_conditions))
        },
        onBack = onBack
    )
}

@Composable
@Preview
internal fun TermsAndConditionsContentPreview() {
    TermsAndConditionsContent(
        url = DEFAULT_URL,
        onBack = {}
    )
}
