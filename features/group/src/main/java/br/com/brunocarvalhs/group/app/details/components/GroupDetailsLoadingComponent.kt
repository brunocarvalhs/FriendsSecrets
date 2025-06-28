package br.com.brunocarvalhs.group.app.details.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.brunocarvalhs.friendssecrets.ui.components.LoadingProgress

@Composable
fun GroupDetailsLoadingComponent(modifier: Modifier) {
    LoadingProgress(
        modifier = modifier.fillMaxSize()
    )
}