package br.com.brunocarvalhs.friendssecrets.commons.security

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings.Secure

object DeviceManager {

    private var cachedDeviceId: String? = null

    /**
     * Retorna o ANDROID_ID, um identificador único de 64 bits (hexadecimal).
     * Ideal para logs de erro e identificação de instalação sem exigir permissões.
     */
    @SuppressLint("HardwareIds")
    fun getDeviceIdentifier(context: Context): String {
        cachedDeviceId?.let { return it }

        val androidId = Secure.getString(
            context.contentResolver,
            Secure.ANDROID_ID
        )

        return (androidId ?: "unknown_device").also {
            cachedDeviceId = it
        }
    }
}