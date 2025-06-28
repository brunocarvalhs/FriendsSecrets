package br.com.brunocarvalhs.auth.app.phoneVerify

import android.app.Activity

sealed class PhoneVerifyIntent {
    data class VerifyCode(val code: String) : PhoneVerifyIntent()
    data class ResendCode(val activity: Activity, val phone: String, val countryCode: String) :
        PhoneVerifyIntent()
}