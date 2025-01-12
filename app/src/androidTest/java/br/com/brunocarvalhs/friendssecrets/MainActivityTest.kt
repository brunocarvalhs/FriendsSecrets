package br.com.brunocarvalhs.friendssecrets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.presentation.MainApp
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        // Configuração inicial do teste, você pode mockar dependências aqui
    }

    @Test
    fun testMainActivity() {
        // Definir o conteúdo da tela de teste
        composeTestRule.setContent {
            FriendsSecretsTheme(
                isThemeRemote = false, // Definir se o tema remoto está habilitado ou não
                themeRemoteProvider = ThemeRemoteProvider(context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().context)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MainApp(navController = navController, toggleManager = ToggleManager(context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().context))
                }
            }
        }

        // Testar se a tela foi renderizada com sucesso verificando um componente, por exemplo, o título ou texto
        composeTestRule.onNodeWithText("Your expected text here").assertExists()

        // Testar se a navegação funciona, por exemplo, clicando em um botão
        composeTestRule.onNodeWithText("Navigate Button Text")
            .performClick()

        // Verificar se a navegação ocorreu corretamente
        composeTestRule.onNodeWithText("Destination Screen Name").assertExists()
    }
}
