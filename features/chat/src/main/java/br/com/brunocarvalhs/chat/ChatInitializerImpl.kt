package br.com.brunocarvalhs.chat

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import javax.inject.Inject

class ChatInitializerImpl @Inject constructor() : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        ChatInitializer.Builder()
            .navController(navController)
            .onBack { navController.popBackStack() }
            .build(navGraphBuilder)
    }
}