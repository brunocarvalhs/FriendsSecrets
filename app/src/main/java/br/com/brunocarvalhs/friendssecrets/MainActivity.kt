package br.com.brunocarvalhs.friendssecrets

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsEvents
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsParams
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleKeys
import br.com.brunocarvalhs.friendssecrets.di.ServiceLocator
import br.com.brunocarvalhs.friendssecrets.presentation.MainApp
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

class MainActivity : FragmentActivity() {

    // Obtém as dependências do ServiceLocator
    private val toggleManager = ServiceLocator.getToggleManager()
    private val themeRemoteProvider = ServiceLocator.getThemeRemoteProvider()
    private val analyticsProvider = ServiceLocator.getAnalyticsProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        enableEdgeToEdge()
        setContent {
            FriendsSecretsTheme(
                isThemeRemote = toggleManager.isFeatureEnabled(ToggleKeys.APP_IS_THEME_REMOTE),
                themeRemoteProvider = themeRemoteProvider
            ) {
                Surface(
                    modifier = Modifier.imePadding().fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController().apply {
                        addOnDestinationChangedListener { _, destination, _ ->
                            analyticsProvider.track(
                                event = AnalyticsEvents.VISUALIZATION,
                                params = mapOf(
                                    AnalyticsParams.SCREEN_NAME to destination.route.toString()
                                )
                            )
                        }
                    }
                    MainApp(
                        activity = this,
                        navController = navController,
                        toggleManager = toggleManager
                    )
                }
            }
        }
    }
}
