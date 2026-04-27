package br.com.brunocarvalhs.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface FeatureInitializer {
    fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController)
}
