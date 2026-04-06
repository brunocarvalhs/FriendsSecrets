package br.com.brunocarvalhs.friendssecrets.commons.security

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings.Secure
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import br.com.brunocarvalhs.friendssecrets.domain.services.DeviceService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) : DeviceService {

    @SuppressLint("HardwareIds")
    private suspend fun getDeviceIdentifier(): String {
        cachedDeviceId?.let {
            Timber.d("Returning cached device ID: $it")
            return it
        }

        return withContext(Dispatchers.IO) {
            synchronized(this) {
                cachedDeviceId ?: run {
                    val androidId = Secure.getString(
                        context.contentResolver,
                        Secure.ANDROID_ID
                    )

                    val finalId = androidId ?: getOrGenerateFallbackId()
                    Timber.d("Generated device ID: $finalId")
                    finalId.also { cachedDeviceId = it }
                }
            }
        }
    }

    private fun getOrGenerateFallbackId(): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val existingId = prefs.getString(KEY_FALLBACK_ID, null)

        return if (existingId != null) {
            Timber.d("Using existing device ID: $existingId")
            existingId
        } else {
            val newId = UUID.randomUUID().toString()
            prefs.edit().putString(KEY_FALLBACK_ID, newId).apply()
            Timber.d("Generated new device ID: $newId")
            newId
        }
    }

    override suspend fun getDeviceId(): String = getDeviceIdentifier()

    companion object {
        @Volatile
        private var cachedDeviceId: String? = null
        private const val PREFS_NAME = "device_prefs"
        private const val KEY_FALLBACK_ID = "fallback_device_id"

        suspend fun getDeviceIdentifier(context: Context): String {
            return DeviceManager(context).getDeviceId()
        }

        fun setCallbackDeviceId(context: Context, callback: (String) -> Unit) {
            ProcessLifecycleOwner.get().lifecycleScope.launch {
                try {
                    val deviceId = DeviceManager(context).getDeviceIdentifier()
                    Timber.d("Device ID: $deviceId")
                    callback(deviceId)
                } catch (e: kotlinx.coroutines.CancellationException) {
                    Timber.e(e, "Error getting device identifier")
                    throw e
                } catch (e: Exception) {
                    Timber.e(e, "Error getting device identifier")
                    callback("unknown_error")
                }
            }
        }
    }
}