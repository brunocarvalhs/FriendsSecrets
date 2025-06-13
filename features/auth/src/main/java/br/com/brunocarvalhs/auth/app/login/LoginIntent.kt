package br.com.brunocarvalhs.auth.app.login

internal sealed class LoginIntent {
    data object Logged : LoginIntent()
    data object Accept : LoginIntent()
    data object AcceptNotRegister : LoginIntent()
    data object PrivacyPolicy : LoginIntent()
    data object TermsOfUse : LoginIntent()
}