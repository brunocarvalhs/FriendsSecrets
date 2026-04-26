package br.com.brunocarvalhs.group.draw

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.core.navigation.FeatureInitializer
import javax.inject.Inject

class DrawInitializerImpl @Inject constructor() : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        DrawInitializer.Builder()
            .navController(navController)
            .onBack {
                navController.popBackStack()
            }
            .build(navGraphBuilder)
    }
}
