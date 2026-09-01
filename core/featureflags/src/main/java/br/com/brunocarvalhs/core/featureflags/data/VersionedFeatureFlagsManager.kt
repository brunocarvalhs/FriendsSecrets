package br.com.brunocarvalhs.core.featureflags.data

import br.com.brunocarvalhs.core.featureflags.domain.AppVersionProvider
import br.com.brunocarvalhs.core.featureflags.domain.FeatureFlagOverride
import br.com.brunocarvalhs.core.featureflags.domain.VersionedFeatureFlagService
import br.com.brunocarvalhs.core.remote.domain.ConfigurationService
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Reads a JSON array of [FeatureFlagOverride] from Remote Config (parameter
 * [REMOTE_CONFIG_KEY]) and uses it to override individual features for the
 * app version currently running, e.g.:
 *
 * ```json
 * [
 *   { "key": "feature_ai_gift_chat", "enabled": false, "version": "3.7.0" }
 * ]
 * ```
 *
 * This lets a specific buggy release be disabled remotely without a store
 * update, while every other version keeps using [isEnabled]'s default.
 */
@Singleton
internal class VersionedFeatureFlagsManager @Inject constructor(
    private val configurationService: ConfigurationService,
    private val appVersionProvider: AppVersionProvider
) : VersionedFeatureFlagService {

    private val json = Json { ignoreUnknownKeys = true }

    override fun isEnabled(key: String, defaultValue: Boolean): Boolean {
        val override = readOverrides()
            .firstOrNull { it.key == key && it.version == appVersionProvider.getVersionName() }

        return override?.enabled ?: defaultValue
    }

    private fun readOverrides(): List<FeatureFlagOverride> {
        val raw = configurationService.getString(REMOTE_CONFIG_KEY, EMPTY_ARRAY)
        return try {
            json.decodeFromString<List<FeatureFlagOverride>>(raw)
        } catch (error: SerializationException) {
            Timber.e(error, "Invalid feature flag overrides JSON")
            emptyList()
        } catch (error: IllegalArgumentException) {
            Timber.e(error, "Invalid feature flag overrides JSON")
            emptyList()
        }
    }

    private companion object {
        const val REMOTE_CONFIG_KEY = "feature_flags_overrides"
        const val EMPTY_ARRAY = "[]"
    }
}
