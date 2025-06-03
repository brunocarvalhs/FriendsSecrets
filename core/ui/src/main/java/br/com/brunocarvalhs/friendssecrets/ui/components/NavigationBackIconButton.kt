package br.com.brunocarvalhs.friendssecrets.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun NavigationBackIconButton(
    navController: NavController? = null,
    onClick: () -> Unit = {}
) {
    IconButton(onClick = { navController?.popBackStack() ?: onClick.invoke() }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back"
        )
    }
}

@Composable
@Preview
private fun NavigationBackIconButtonPreview() {
    NavigationBackIconButton()
}