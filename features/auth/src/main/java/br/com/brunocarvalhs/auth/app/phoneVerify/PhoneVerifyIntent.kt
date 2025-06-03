package br.com.brunocarvalhs.auth.app.phoneVerify

sealed class PhoneVerifyIntent {
    data class VerifyCode(val code: String) : PhoneVerifyIntent()
}