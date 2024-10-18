package br.com.brunocarvalhs.friendssecrets.commons.toggle

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object ToggleManager {

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        setupRemoteConfig()
    }

    private fun setupRemoteConfig() {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)

        // Definindo parâmetros padrão (exemplo)
        val defaultConfigMap = mapOf(
            "feature_x_enabled" to false,
            "feature_y_enabled" to true
        )

        remoteConfig.setDefaultsAsync(defaultConfigMap)

        fetchAndActivate()
    }

    private fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Valores atualizados com sucesso.")
                } else {
                    println("Falha ao atualizar valores.")
                }
            }
    }

    fun isFeatureEnabled(featureKey: String): Boolean {
        return remoteConfig.getBoolean(featureKey)
    }
}
