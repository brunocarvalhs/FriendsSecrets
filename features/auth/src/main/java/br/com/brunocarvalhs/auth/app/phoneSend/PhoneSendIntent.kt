package br.com.brunocarvalhs.auth.app.phoneSend

sealed class PhoneSendIntent {
    data class SendCode(val phone: String, val countryCode: String) : PhoneSendIntent()
}