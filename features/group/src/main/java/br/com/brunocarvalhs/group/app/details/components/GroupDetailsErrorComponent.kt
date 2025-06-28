package br.com.brunocarvalhs.group.app.details.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import br.com.brunocarvalhs.friendssecrets.ui.components.ErrorComponent
import br.com.brunocarvalhs.group.app.details.GroupDetailsUiState

@Composable
fun GroupDetailsErrorComponent(
    modifier: Modifier,
    navController: NavController,
    uiState: GroupDetailsUiState.Error
) {
    ErrorComponent(
        modifier = modifier.fillMaxSize(),
        message = uiState.message,
        onBack = { navController.popBackStack() }
    )
}