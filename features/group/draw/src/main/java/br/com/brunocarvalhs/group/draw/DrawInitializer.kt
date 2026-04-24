package br.com.brunocarvalhs.group.draw

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.friendssecrets.core.navigation.DrawGraph
import br.com.brunocarvalhs.group.draw.app.presentation.DrawScreen
import br.com.brunocarvalhs.group.draw.app.presentation.DrawViewModel
import br.com.brunocarvalhs.group.draw.commons.navigation.DrawRouter
import com.google.firebase.perf.metrics.AddTrace
import kotlin.properties.Delegates

class DrawInitializer(private val builder: Builder) {

    @AddTrace(name = "DrawInitializer.build", enabled = true)
    fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.navigation<DrawGraph>(
            startDestination = DrawRouter,
            typeMap = DrawGraph.typeMap
        ) {
            composable<DrawRouter> {
                val viewModel = hiltViewModel<DrawViewModel>()
                DrawScreen(
                    viewModel = viewModel,
                    onBack = builder.onBack
                )
            }
        }
    }

    class Builder {
        internal var navController: NavHostController by Delegates.notNull()
        internal var onBack: () -> Unit = { }

        @AddTrace(name = "DrawInitializer.Builder.navController", enabled = true)
        fun navController(navController: NavHostController) = apply {
            this.navController = navController
        }

        @AddTrace(name = "DrawInitializer.Builder.onBack", enabled = true)
        fun onBack(onBack: () -> Unit) = apply {
            this.onBack = onBack
        }

        @AddTrace(name = "DrawInitializer.Builder.build", enabled = true)
        fun build(navGraphBuilder: NavGraphBuilder): DrawInitializer =
            DrawInitializer(this).also { it.build(navGraphBuilder) }
    }
}