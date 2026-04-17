package br.com.brunocarvalhs.settings.app.faq

import androidx.lifecycle.ViewModel
import br.com.brunocarvalhs.friendssecrets.domain.services.ConfigurationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class FAQViewModel @Inject constructor(
    private val configurationService: ConfigurationService
) : ViewModel() {

    private val _url = MutableStateFlow(
        configurationService.getString(
            key = URL_FAQ,
            defaultValue = DEFAULT_URL
        )
    )
    val url: StateFlow<String> = _url.asStateFlow()

    companion object {
        private const val URL_FAQ = "url_faq"
        private const val DEFAULT_URL = "https://github.com/brunocarvalhs/FriendsSecrets/wiki/FAQ"
    }
}
