package br.com.brunocarvalhs.friendssecrets.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge()
        setContent {
            FriendsSecretsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController().apply {
                        addOnDestinationChangedListener { _, destination, _ ->
                            AnalyticsProvider.track(
                                event = AnalyticsProvider.Event.VISUALIZATION,
                                params = mapOf(
                                    AnalyticsProvider.Param.SCREEN_NAME to destination.route.toString()
                                )
                            )
                        }
                    }
                    MainApp(navController)
                }
            }
        }
    }
}