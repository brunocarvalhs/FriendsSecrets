package br.com.brunocarvalhs.chat

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.chat.commons.navigation.ChatGraphRouter
import br.com.brunocarvalhs.chat.commons.navigation.ChatRouter
import com.google.firebase.perf.metrics.AddTrace
import kotlin.properties.Delegates

class ChatInitializer(private val builder: Builder) {

    fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.navigation<ChatGraphRouter>(startDestination = ChatRouter) {
            composable<ChatRouter> {

            }
        }
    }

    class Builder {
        internal var navController: NavHostController by Delegates.notNull()
        internal var onBack: () -> Unit = {}

        @AddTrace(name = "ChatInitializer.Builder.navController", enabled = true)
        fun navController(navController: NavHostController) = apply {
            this.navController = navController
        }

        @AddTrace(name = "ChatInitializer.Builder.onBack", enabled = true)
        fun onBack(onBack: () -> Unit) = apply {
            this.onBack = onBack
        }

        @AddTrace(name = "ChatInitializer.Builder.build", enabled = true)
        fun build(navGraphBuilder: NavGraphBuilder): ChatInitializer =
            ChatInitializer(this).also { it.build(navGraphBuilder) }
    }
}