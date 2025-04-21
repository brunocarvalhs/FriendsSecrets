package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.LocalDimensions

/**
 * Componente de layout de tela padronizado para uso em toda a aplicação.
 * Isso garante consistência visual e facilita a manutenção.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreenLayout(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    isLoading: Boolean = false,
    error: String? = null,
    onRetry: (() -> Unit)? = null,
    isScrollable: Boolean = true,
    content: @Composable (ColumnScope.() -> Unit)
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .then(if (isScrollable) Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) else Modifier),
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = navigationIcon ?: {},
                actions = { actions?.invoke() },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = { floatingActionButton?.invoke() }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingProgress()
                    }
                }
                error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorComponent(
                            message = error,
                            onRetry = onRetry
                        )
                    }
                }
                else -> {
                    val columnModifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = LocalDimensions.current.spacingMedium)
                        .then(
                            if (isScrollable) {
                                Modifier.verticalScroll(rememberScrollState())
                            } else {
                                Modifier
                            }
                        )
                    
                    Column(
                        modifier = columnModifier
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FriendsScreenLayoutPreview() {
    FriendsSecretsTheme {
        FriendsScreenLayout(
            title = "Tela de Exemplo",
            isScrollable = true
        ) {
            repeat(10) { index ->
                FriendsCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Item $index",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Este é um exemplo de item para demonstrar o layout da tela.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FriendsScreenLayoutLoadingPreview() {
    FriendsSecretsTheme {
        FriendsScreenLayout(
            title = "Tela de Carregamento",
            isLoading = true
        ) {
            // Conteúdo não será exibido durante o carregamento
        }
    }
}

@Preview
@Composable
private fun FriendsScreenLayoutErrorPreview() {
    FriendsSecretsTheme {
        FriendsScreenLayout(
            title = "Tela de Erro",
            error = "Ocorreu um erro ao carregar os dados. Por favor, tente novamente.",
            onRetry = {}
        ) {
            // Conteúdo não será exibido durante o erro
        }
    }
}