package br.com.brunocarvalhs.friendssecrets

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.commons.theme.remote.ThemeRemoteProvider
import br.com.brunocarvalhs.friendssecrets.commons.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.friendssecrets.domain.services.BiometricService
import br.com.brunocarvalhs.friendssecrets.domain.services.ThemeService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var themeService: ThemeService
    @Inject
    lateinit var themeRemoteProvider: ThemeRemoteProvider
    @Inject
    lateinit var biometricService: BiometricService
    @Inject
    lateinit var featureInitializers: Set<@JvmSuppressWildcards FeatureInitializer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
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
                    navController.MainApp(
                        isBiometric = biometricService.isBiometricPromptEnabled.collectAsState().value,
                        initializers = featureInitializers
                    )
                }
            }
        }
    }
}