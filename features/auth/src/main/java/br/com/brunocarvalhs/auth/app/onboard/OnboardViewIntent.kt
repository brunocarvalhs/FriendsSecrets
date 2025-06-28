package br.com.brunocarvalhs.auth.app.onboard

sealed interface OnboardViewIntent {
    data object FetchData : OnboardViewIntent
}