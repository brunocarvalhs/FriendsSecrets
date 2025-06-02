package br.com.brunocarvalhs.auth.app.onboard

sealed interface OnboardUiState {
    data object Loading : OnboardUiState
    data class Idle(val onboard: List<OnboardViewModel.Onboarding>) : OnboardUiState
    data class Success(val onboard: List<OnboardViewModel.Onboarding>) : OnboardUiState
}