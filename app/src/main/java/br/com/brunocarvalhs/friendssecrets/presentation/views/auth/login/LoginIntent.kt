package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.login

sealed class LoginIntent {
    data object Accept : LoginIntent()
    data object AcceptNotRegister : LoginIntent()
    data object PrivacyPolicy : LoginIntent()
    data object TermsOfUse : LoginIntent()
}