package br.com.brunocarvalhs.chat

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.chat.app.presentation.AiGiftChatScreen
import br.com.brunocarvalhs.chat.app.presentation.AiGiftChatViewModel
import br.com.brunocarvalhs.chat.commons.navigation.AiGiftChatRouter
import br.com.brunocarvalhs.core.navigation.routers.AiGiftChatGraph
import kotlin.properties.Delegates

class AiGiftChatInitializer(private val builder: Builder) {

    fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.navigation<AiGiftChatGraph>(
            startDestination = AiGiftChatRouter,
            typeMap = AiGiftChatGraph.typeMap,
        ) {
            composable<AiGiftChatRouter> {
                val viewModel = hiltViewModel<AiGiftChatViewModel>()
                AiGiftChatScreen(
                    viewModel = viewModel,
                    onBack = builder.onBack
                )
            }
        }
    }

    class Builder {
        internal var navController: NavHostController by Delegates.notNull()
        internal var onBack: () -> Unit = {}

        fun navController(navController: NavHostController) = apply {
            this.navController = navController
        }

        fun onBack(onBack: () -> Unit) = apply {
            this.onBack = onBack
        }

        fun build(navGraphBuilder: NavGraphBuilder): AiGiftChatInitializer =
            AiGiftChatInitializer(this).also { it.build(navGraphBuilder) }
    }
}
