package br.com.brunocarvalhs.friendssecrets.presentation.views.home.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

sealed interface LoginIntent {
    data class GoogleAuth(val launcher: ActivityResultLauncher<Intent>) : LoginIntent
    data class FacebookAuth(val launcher: ActivityResultLauncher<Intent>) : LoginIntent
    data class PhoneAuth(val launcher: ActivityResultLauncher<Intent>) : LoginIntent
    data class EmailAuth(val launcher: ActivityResultLauncher<Intent>) : LoginIntent

    data class Success(val event: () -> Unit) : LoginIntent
    data class Error(val message: String) : LoginIntent
}
