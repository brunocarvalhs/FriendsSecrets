package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsUiState

@Composable
fun GroupDetailsErrorComponent(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiState: GroupDetailsUiState.Error,
) {
    ErrorComponent(modifier = modifier
        .fillMaxSize(),
        message = uiState.message,
        onBack = { navController.popBackStack() })
}

@Composable
@Preview
private fun GroupDetailsErrorComponentPreview() {
    GroupDetailsErrorComponent(
        navController = rememberNavController(),
        uiState = GroupDetailsUiState.Error(message = "Error"),
    )
}