package br.com.brunocarvalhs.biometric.app.presentation

import androidx.fragment.app.FragmentActivity

sealed interface BiometricIntent {
    data class Authenticate(val activity: FragmentActivity) : BiometricIntent
}
