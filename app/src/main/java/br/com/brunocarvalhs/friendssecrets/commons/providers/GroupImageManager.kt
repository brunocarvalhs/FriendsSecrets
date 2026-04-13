package br.com.brunocarvalhs.friendssecrets.commons.providers

import br.com.brunocarvalhs.friendssecrets.domain.services.GroupImageService
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupImageManager @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : GroupImageService {

    private val defaultPhotos = listOf(
        "https://cdn-icons-png.flaticon.com/512/2324/2324505.png",
        "https://cdn-icons-png.flaticon.com/512/2666/2666778.png",
        "https://cdn-icons-png.flaticon.com/512/2436/2436725.png",
        "https://cdn-icons-png.flaticon.com/512/833/833472.png",
        "https://cdn-icons-png.flaticon.com/512/3661/3661849.png",
        "https://cdn-icons-png.flaticon.com/512/5323/5323490.png"
    )

    private val _availablePhotos = MutableStateFlow(defaultPhotos)
    override val availablePhotos: StateFlow<List<String>> = _availablePhotos.asStateFlow()

    init {
        setupRemoteConfig()
    }

    private fun setupRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        
        val defaults = mapOf(PHOTOS_KEY to Json.encodeToString(defaultPhotos))
        remoteConfig.setDefaultsAsync(defaults)

        fetchRemotePhotos()
    }

    private fun fetchRemotePhotos() {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val jsonPhotos = remoteConfig.getString(PHOTOS_KEY)
                try {
                    val decoded = Json.decodeFromString<List<String>>(jsonPhotos)
                    if (decoded.isNotEmpty()) {
                        _availablePhotos.value = decoded
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error decoding Remote Config photos")
                }
            }
        }
    }

    override fun getDefault(): String = _availablePhotos.value.first()

    companion object {
        private const val PHOTOS_KEY = "group_available_photos"
    }
}
