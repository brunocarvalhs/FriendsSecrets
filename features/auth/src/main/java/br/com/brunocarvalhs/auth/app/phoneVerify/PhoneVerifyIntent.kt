package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneVerify

sealed class PhoneVerifyIntent {
    data class VerifyCode(val code: String) : PhoneVerifyIntent()
}