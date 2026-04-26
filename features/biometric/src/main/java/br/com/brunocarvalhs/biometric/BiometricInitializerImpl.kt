package br.com.brunocarvalhs.biometric

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.core.navigation.routers.BiometricGraph
import br.com.brunocarvalhs.core.navigation.CommonNavigator
import br.com.brunocarvalhs.core.navigation.FeatureInitializer
import javax.inject.Inject

class BiometricInitializerImpl @Inject constructor(
    private val commonNavigator: CommonNavigator
) : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        BiometricInitializer.Builder()
            .navController(navController)
            .onSuccess {
                commonNavigator.navigateToGroupList(
                    navController = navController,
                    popUpTo = BiometricGraph::class,
                    inclusive = true
                )
            }
            .build(navGraphBuilder)
    }
}
