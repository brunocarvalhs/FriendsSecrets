package br.com.brunocarvalhs.settings.app.faq

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
internal class FAQViewModel @Inject constructor(
    private val configurationService: ConfigurationService,
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val _url = MutableStateFlow(
        configurationService.getString(
            key = URL_FAQ,
            defaultValue = DEFAULT_URL
        )
    )
    val url: StateFlow<String> = _url.asStateFlow()

    init {
        loadUrl()
    }

    @AddTrace(name = "FAQViewModel.loadUrl", enabled = true)
    private fun loadUrl() {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(
                AnalyticsParam.ACTION to "load_url"
            )
        )
        _url.value = configurationService.getString(
            key = URL_FAQ,
            defaultValue = DEFAULT_URL
        )
    }

    companion object {
        private const val URL_FAQ = "url_faq"
        const val DEFAULT_URL = "https://forms.office.com/r/rJ0WHvvPnd"
    }
}
