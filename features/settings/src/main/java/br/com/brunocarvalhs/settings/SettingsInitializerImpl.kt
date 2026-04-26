package br.com.brunocarvalhs.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.core.navigation.FeatureInitializer
import javax.inject.Inject

class SettingsInitializerImpl @Inject constructor() : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        SettingsInitializer.Builder()
            .navController(navController)
            .onBack {
                navController.popBackStack()
            }
            .build(navGraphBuilder)
    }
}
