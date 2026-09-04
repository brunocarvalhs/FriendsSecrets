package br.com.brunocarvalhs.friendssecrets

import AnalyticsParam
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.biometric.BiometricService
import br.com.brunocarvalhs.core.analytics.AnalyticsService
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent
import br.com.brunocarvalhs.core.navigation.DeepLinkHandler
import br.com.brunocarvalhs.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.core.remote.domain.ThemeRemote
import br.com.brunocarvalhs.core.remote.domain.ThemeService
import br.com.brunocarvalhs.core.ui.theme.FriendsSecretsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var themeService: ThemeService
    @Inject
    lateinit var themeRemoteProvider: ThemeRemote
    @Inject
    lateinit var biometricService: BiometricService
    @Inject
    lateinit var analyticsService: AnalyticsService
    @Inject
    lateinit var featureInitializers: Set<@JvmSuppressWildcards FeatureInitializer>
    @Inject
    lateinit var deepLinkHandler: DeepLinkHandler
    @Volatile
    private var isThemeReady = false

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            !isThemeReady
        }

        super.onCreate(savedInstanceState)

        deepLinkHandler.handle(intent?.data)

        lifecycleScope.launch {
            themeService.initialize()
            isThemeReady = true
        }

        enableEdgeToEdge()

        setContent {
            FriendsSecretsTheme(
                themeService = themeService,
                themeRemoteProvider = themeRemoteProvider,
            ) {
                Surface(
                    modifier = Modifier
                        .imePadding()
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()

                    LaunchedEffect(navController) {
                        navController.currentBackStackEntryFlow.collect { backStackEntry ->
                            analyticsService.logEvent(
                                name = AnalyticsEvent.VIEW,
                                params = mapOf(
                                    AnalyticsParam.SCREEN to backStackEntry.destination.route
                                )
                            )
                        }
                    }

                    navController.mainApp(
                        isBiometric = biometricService.isBiometricPromptEnabled.collectAsState().value,
                        initializers = featureInitializers
                    )
                }
            }
        }
    }
}
