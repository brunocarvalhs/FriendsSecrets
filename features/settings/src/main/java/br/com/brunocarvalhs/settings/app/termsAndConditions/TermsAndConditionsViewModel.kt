package br.com.brunocarvalhs.settings.app.termsAndConditions

import AnalyticsParam
import androidx.lifecycle.ViewModel
import br.com.brunocarvalhs.core.analytics.AnalyticsService
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent
import br.com.brunocarvalhs.core.remote.domain.ConfigurationService
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class TermsAndConditionsViewModel @Inject constructor(
    private val configurationService: ConfigurationService,
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val _url = MutableStateFlow(
        configurationService.getString(
            key = URL_TERMS_AND_CONDITIONS,
            defaultValue = DEFAULT_URL
        )
    )
    val url: StateFlow<String> = _url.asStateFlow()

    init {
        loadUrl()
    }

    @AddTrace(name = "TermsAndConditionsViewModel.loadUrl", enabled = true)
    private fun loadUrl() {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(
                AnalyticsParam.ACTION to "load_url"
            )
        )
        _url.value = configurationService.getString(
            key = URL_TERMS_AND_CONDITIONS,
            defaultValue = DEFAULT_URL
        )
    }

    companion object {
        private const val URL_TERMS_AND_CONDITIONS = "url_terms_and_conditions"

        @Suppress("MaxLineLength")
        const val DEFAULT_URL =
            "https://raw.githubusercontent.com/brunocarvalhs/FriendsSecrets/refs/heads/develop/docs/TermsEndConditions.md"
    }
}
