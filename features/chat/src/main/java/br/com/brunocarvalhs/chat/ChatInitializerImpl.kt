package br.com.brunocarvalhs.chat

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.chat.app.presentation.ChatScreen
import br.com.brunocarvalhs.chat.app.presentation.ChatViewModel
import br.com.brunocarvalhs.chat.commons.navigation.ChatGraphRouter
import br.com.brunocarvalhs.chat.commons.navigation.ChatRouter
import br.com.brunocarvalhs.chat.commons.navigation.GenerativeRouter
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import javax.inject.Inject

class ChatInitializerImpl @Inject constructor() : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.navigation<ChatGraphRouter>(
            startDestination = ChatRouter,
            typeMap = ChatGraphRouter.typeMap,
        ) {
            composable<ChatRouter> {
                val viewModel = hiltViewModel<ChatViewModel>()
                ChatScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<GenerativeRouter> {

            }
        }
    }
}