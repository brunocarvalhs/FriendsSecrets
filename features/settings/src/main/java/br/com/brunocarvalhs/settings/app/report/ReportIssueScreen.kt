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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.brunocarvalhs.friendssecrets.ui.components.WebViewContainer
import br.com.brunocarvalhs.settings.R

@Composable
fun ReportIssueScreen(
    onBack: () -> Unit
) {
    ReportIssueContent(
        onBack = onBack
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportIssueContent(
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
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            WebViewContainer(
                url = "https://github.com/brunocarvalhs/FriendsSecrets/issues/new",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
@Preview
private fun ReportIssuePreview() {
    ReportIssueContent(
        onBack = {}
    )
}