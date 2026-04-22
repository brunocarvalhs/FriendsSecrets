package br.com.brunocarvalhs.friendssecrets.commons.providers

import br.com.brunocarvalhs.group.create.app.domain.services.GroupImageService
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
        "https://images.unsplash.com/photo-1549465220-1a8b9238cd48?w=400&h=400&fit=crop",
        "https://images.unsplash.com/photo-1512909006721-3d6018887383?w=400&h=400&fit=crop",
        "https://images.unsplash.com/photo-1482517967863-00e15c9b44be?w=400&h=400&fit=crop",
        "https://images.unsplash.com/photo-1543589077-47d81606c1bf?w=400&h=400&fit=crop",
        "https://images.unsplash.com/photo-1512389142860-9c449e58a543?w=400&h=400&fit=crop",
        "https://images.unsplash.com/photo-1608889175123-8ee362201f81?w=400&h=400&fit=crop",
        "https://images.unsplash.com/photo-1579202673506-ca3ce28943ef?w=400&h=400&fit=crop",
        "https://images.unsplash.com/photo-1542838132-92c53300491e?w=400&h=400&fit=crop",
        "https://images.unsplash.com/photo-1607344645866-009c320b63e0?w=400&h=400&fit=crop",
        "https://images.unsplash.com/photo-1513885535751-8b9238bd345a?w=400&h=400&fit=crop"
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
