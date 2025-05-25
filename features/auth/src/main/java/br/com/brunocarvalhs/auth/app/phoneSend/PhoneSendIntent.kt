package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneSend

sealed class PhoneSendIntent {
    data class SendCode(val phone: String, val countryCode: String) : PhoneSendIntent()
}