package br.com.brunocarvalhs.settings.app.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.brunocarvalhs.settings.R
import br.com.brunocarvalhs.settings.app.report.components.WebViewContainer

@Composable
internal fun ReportIssueScreen(
    onBack: () -> Unit,
    viewModel: ReportIssueViewModel = hiltViewModel()
) {
    val url by viewModel.url.collectAsState()

    ReportIssueContent(
        url = url,
        onBack = onBack
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportIssueContent(
    url: String,
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = stringResource(R.string.title_report_an_issue))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            WebViewContainer(
                url = url,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
@Preview
private fun ReportIssuePreview() {
    ReportIssueContent(
        url = "https://github.com/brunocarvalhs/FriendsSecrets/issues/new",
        onBack = {}
    )
}
