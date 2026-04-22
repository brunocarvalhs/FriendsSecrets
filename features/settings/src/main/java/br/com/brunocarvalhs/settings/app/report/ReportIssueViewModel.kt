package br.com.brunocarvalhs.settings.app.report

import androidx.lifecycle.ViewModel
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.ConfigurationService
import br.com.brunocarvalhs.settings.app.list.SettingsAnalytics
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class ReportIssueViewModel @Inject constructor(
    private val configurationService: ConfigurationService,
    private val analytics: SettingsAnalytics
) : ViewModel() {

    private val _url = MutableStateFlow(
        configurationService.getString(
            key = URL_REPORT_ISSUE,
            defaultValue = DEFAULT_URL
        )
    )
    val url: StateFlow<String> = _url.asStateFlow()

    init {
        analytics.trackReportIssueView()
        loadUrl()
    }

    @AddTrace(name = "ReportIssueViewModel.loadUrl", enabled = true)
    private fun loadUrl() {
        _url.value = configurationService.getString(
            key = URL_REPORT_ISSUE,
            defaultValue = DEFAULT_URL
        )
    }

    companion object {
        private const val URL_REPORT_ISSUE = "url_report_issue"
        private const val DEFAULT_URL = "https://github.com/brunocarvalhs/FriendsSecrets/issues/new"
    }
}
