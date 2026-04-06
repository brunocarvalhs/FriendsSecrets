package br.com.brunocarvalhs.group.draw.app.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import br.com.brunocarvalhs.group.draw.commons.navigation.DrawGraphRouter
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DrawViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args = savedStateHandle.toRoute<DrawGraphRouter>(DrawGraphRouter.typeMap)

    private val _uiState: MutableStateFlow<DrawUiState> =
        MutableStateFlow(DrawUiState(
            groupModel = args.group
        ))

    val uiState: StateFlow<DrawUiState> =
        _uiState.asStateFlow()

    @AddTrace(name = "DrawViewModel.eventIntent", enabled = true)
    fun handleIntent(intent: DrawIntent) = when (intent) {
        else -> {

        }
    }

    @AddTrace(name = "DrawViewModel.fetchDraw", enabled = true)
    private fun fetchDraw(group: String, code: String? = null) {

    }
}