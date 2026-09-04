package br.com.brunocarvalhs.chat

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.core.navigation.FeatureInitializer
import javax.inject.Inject

class AiGiftChatInitializerImpl @Inject constructor() : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        AiGiftChatInitializer.Builder()
            .navController(navController)
            .onBack { navController.popBackStack() }
            .build(navGraphBuilder)
    }
}
