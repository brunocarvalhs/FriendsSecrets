package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.multLogin

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

sealed class MultiLoginViewIntent {
    data class GoogleAuth(val launcher: ActivityResultLauncher<Intent>) : MultiLoginViewIntent()
    data class PhoneAuth(val launcher: ActivityResultLauncher<Intent>) : MultiLoginViewIntent()
    data class EmailAuth(val launcher: ActivityResultLauncher<Intent>) : MultiLoginViewIntent()

    data class Success(val event: () -> Unit) : MultiLoginViewIntent()
    data class Error(val message: String) : MultiLoginViewIntent()
}
