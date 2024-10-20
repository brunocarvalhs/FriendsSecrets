package br.com.brunocarvalhs.friendssecrets.presentation.views.home.onboard

sealed interface OnboardViewIntent {
    data object FetchData : OnboardViewIntent
}