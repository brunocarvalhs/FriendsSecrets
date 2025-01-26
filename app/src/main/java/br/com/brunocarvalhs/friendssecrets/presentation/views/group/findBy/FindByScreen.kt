package br.com.brunocarvalhs.friendssecrets.presentation.views.group.findBy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.create.GroupCreateUiState

@Composable
fun FindByScreen(
    navController: NavController
) {
    FindByContent(
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindByContent(
    navController: NavController
) {
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {

            },
            navigationIcon = {
                NavigationBackIconButton(navController = navController)
            },
        )
    }, floatingActionButton = {

    }) { innerPading ->
        Column(modifier = Modifier.padding(innerPading)) {

        }
    }
}

@Composable
@Preview
fun FindByScreenPreview() {

}