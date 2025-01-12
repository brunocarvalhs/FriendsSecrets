package br.com.brunocarvalhs.friendssecrets.presentation.views.home.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.remote.RemoteProvider
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.reflect.Type
import java.util.Locale

class OnboardViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<OnboardUiState> =
        MutableStateFlow(OnboardUiState.Idle(default))

    val uiState: StateFlow<OnboardUiState> =
        _uiState.asStateFlow()

    sealed class ImageSource {
        data class Url(val url: String) : ImageSource()
        data class Resource(val resId: Int) : ImageSource()
        data class Animation(val json: String) : ImageSource()
    }

    data class Onboarding(
        @SerializedName("image_res") val imageSource: ImageSource,
        @SerializedName("title") val title: String = "",
        @SerializedName("description") val description: String = "",
    )

    private class ImageSourceDeserializer : JsonDeserializer<ImageSource> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?,
        ): ImageSource {
            return if (json != null && json.isJsonPrimitive) {
                val jsonPrimitive = json.asJsonPrimitive
                if (jsonPrimitive.isString) {
                    ImageSource.Url(jsonPrimitive.asString)
                } else if (jsonPrimitive.isNumber) {
                    ImageSource.Resource(jsonPrimitive.asInt)
                } else {
                    throw JsonParseException("Tipo de ImageSource inválido.")
                }
            } else {
                throw JsonParseException("Elemento JSON inválido.")
            }
        }
    }

    fun onEvent(intent: OnboardViewIntent) {
        when (intent) {
            is OnboardViewIntent.FetchData -> fetchGroups()
        }
    }

    private fun fetchGroups() {
        _uiState.value = OnboardUiState.Loading
        viewModelScope.launch {
            try {
                val gson: Gson = GsonBuilder()
                    .registerTypeAdapter(ImageSource::class.java, ImageSourceDeserializer())
                    .create()

                val data: String =
                    RemoteProvider().getAsyncString("${ONBOARDING_PAGES}_${getSystemLanguage()}")

                val list: List<Onboarding> =
                    gson.fromJson(data, object : TypeToken<List<Onboarding>>() {}.type)

                _uiState.value = OnboardUiState.Success(list)
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.value = OnboardUiState.Success(default)
            }
        }
    }

    private fun getSystemLanguage(): String {
        val systemLocale = Locale.getDefault()
        return systemLocale.language.takeIf { it.isNotEmpty() } ?: "en"
    }

    companion object {
        const val ONBOARDING_PAGES = "onboarding_pages"
        private val context = CustomApplication.getInstance()

        val default = listOf(
            Onboarding(
                imageSource = ImageSource.Resource(R.drawable.ic_logo__friends_secrets),
                title = context.getString(R.string.onboarding_screen_title),
                description = context.getString(R.string.onboarding_screen_description)
            )
        )

        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    OnboardViewModel()
                }
            }
    }
}