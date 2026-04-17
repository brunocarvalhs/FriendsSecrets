package br.com.brunocarvalhs.settings.app.report

import androidx.lifecycle.ViewModel
import br.com.brunocarvalhs.friendssecrets.domain.services.ConfigurationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class ReportIssueViewModel @Inject constructor(
    private val configurationService: ConfigurationService
) : ViewModel() {

    private val _url = MutableStateFlow(
        configurationService.getString(
            key = URL_REPORT_ISSUE,
            defaultValue = DEFAULT_URL
        )
    )
    val url: StateFlow<String> = _url.asStateFlow()

    companion object {
        private const val URL_REPORT_ISSUE = "url_report_issue"
        private const val DEFAULT_URL = "https://github.com/brunocarvalhs/FriendsSecrets/issues/new"
    }
}
