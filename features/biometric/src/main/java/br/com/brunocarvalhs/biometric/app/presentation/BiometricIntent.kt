package br.com.brunocarvalhs.biometric.app.presentation

import androidx.fragment.app.FragmentActivity

internal sealed interface BiometricIntent {
    data class Authenticate(val activity: FragmentActivity) : BiometricIntent
}
