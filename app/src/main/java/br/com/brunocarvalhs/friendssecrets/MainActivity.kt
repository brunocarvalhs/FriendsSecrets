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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.biometric.BiometricService
import br.com.brunocarvalhs.core.remote.domain.ThemeRemote
import br.com.brunocarvalhs.core.remote.domain.ThemeService
import br.com.brunocarvalhs.core.navigation.FeatureInitializer
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
    lateinit var featureInitializers: Set<@JvmSuppressWildcards FeatureInitializer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        enableEdgeToEdge()

        lifecycleScope.launch {
            themeService.initialize()
        }

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
                    navController.mainApp(
                        isBiometric = biometricService.isBiometricPromptEnabled.collectAsState().value,
                        initializers = featureInitializers
                    )
                }
            }
        }
    }
}
