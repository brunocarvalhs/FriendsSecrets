package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.LoadingProgress

@Composable
fun GroupDetailsLoadingComponent(
    modifier: Modifier = Modifier
) {
    LoadingProgress(
        modifier = modifier.fillMaxSize()
    )
}

@Composable
@Preview
private fun GroupDetailsLoadingComponentPreview() {
    GroupDetailsLoadingComponent()
}