package br.com.brunocarvalhs.auth.app.phoneSend

import android.app.Activity

sealed class PhoneSendIntent {
    data class SendCode(val activity: Activity, val phone: String, val countryCode: String) :
        PhoneSendIntent()
}