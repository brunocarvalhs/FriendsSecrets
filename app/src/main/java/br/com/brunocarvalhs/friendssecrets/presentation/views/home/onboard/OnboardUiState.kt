package br.com.brunocarvalhs.friendssecrets.presentation.views.home.onboard

sealed interface OnboardUiState {
    data object Loading : OnboardUiState
    data class Idle(val onboard: List<OnboardViewModel.Onboarding>) : OnboardUiState
    data class Success(val onboard: List<OnboardViewModel.Onboarding>) : OnboardUiState
}