package br.com.brunocarvalhs.group.draw

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.draw.app.presentation.DrawScreen
import br.com.brunocarvalhs.group.draw.app.presentation.DrawViewModel
import br.com.brunocarvalhs.group.draw.commons.navigation.DrawGraphRouter
import br.com.brunocarvalhs.group.draw.commons.navigation.DrawRouter
import javax.inject.Inject

class DrawInitializerImpl @Inject constructor() : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.navigation<DrawGraphRouter>(
            startDestination = DrawRouter,
            typeMap = DrawGraphRouter.typeMap
        ) {
            composable<DrawRouter> {
                val viewModel = hiltViewModel<DrawViewModel>()
                DrawScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}