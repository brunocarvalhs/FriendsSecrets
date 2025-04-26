# Sistema de Componentes UI do FriendsSecrets

Este documento descreve o sistema de componentes UI padronizados para o aplicativo FriendsSecrets, projetado para melhorar a manutenção e consistência visual.

## Visão Geral

O sistema de componentes UI do FriendsSecrets foi criado para:

1. **Garantir consistência visual** em todo o aplicativo
2. **Facilitar a manutenção** reduzindo a duplicação de código
3. **Melhorar a produtividade** dos desenvolvedores
4. **Simplificar atualizações** de design

## Estrutura do Sistema

### Dimensões Padronizadas

Todas as dimensões (espaçamentos, elevações, tamanhos, etc.) são definidas centralmente em `Dimensions.kt` e acessíveis através de `LocalDimensions.current`. Isso garante consistência e facilita ajustes globais.

```kotlin
// Exemplo de uso
Text(
    modifier = Modifier.padding(
        horizontal = LocalDimensions.current.spacingMedium,
        vertical = LocalDimensions.current.spacingSmall
    )
)
```

### Componentes Básicos

#### FriendsButton

Botões padronizados com variantes:
- `FriendsButton`: Botão primário preenchido
- `FriendsOutlinedButton`: Botão secundário com contorno

```kotlin
FriendsButton(
    text = "Continuar",
    onClick = { /* ação */ }
)

FriendsOutlinedButton(
    text = "Cancelar",
    onClick = { /* ação */ }
)
```

#### FriendsTextField

Campos de texto padronizados com variantes:
- `FriendsTextField`: Campo de texto padrão
- `FriendsOutlinedTextField`: Campo de texto com contorno

```kotlin
FriendsTextField(
    value = state.text,
    onValueChange = { viewModel.updateText(it) },
    label = "Nome",
    isError = state.hasError,
    errorMessage = state.errorMessage
)
```

#### FriendsCard

Cards padronizados com variantes:
- `FriendsCard`: Card com elevação
- `FriendsOutlinedCard`: Card com contorno

```kotlin
FriendsCard {
    Text(text = "Título do Card")
    Text(text = "Conteúdo do card...")
}
```

### Layouts de Tela

#### FriendsScreenLayout

Layout de tela padronizado que gerencia:
- Barra superior
- Estados de carregamento
- Estados de erro
- Conteúdo com rolagem

```kotlin
FriendsScreenLayout(
    title = "Título da Tela",
    isLoading = uiState.isLoading,
    error = uiState.error,
    onRetry = { viewModel.retry() }
) {
    // Conteúdo da tela
}
```

## Boas Práticas

1. **Sempre use os componentes padronizados** em vez de criar novos
2. **Respeite as dimensões padronizadas** definidas em `Dimensions.kt`
3. **Adicione previews** para todos os novos componentes
4. **Documente** qualquer novo componente adicionado
5. **Mantenha a consistência** com o design system

## Extensão do Sistema

Para adicionar novos componentes:

1. Crie um novo arquivo com o prefixo `Friends` (ex: `FriendsDialog.kt`)
2. Implemente o componente seguindo o padrão existente
3. Adicione previews para demonstrar o uso
4. Atualize esta documentação

## Exemplos de Uso

Veja os arquivos de componentes existentes para exemplos de implementação e uso:
- `FriendsButton.kt`
- `FriendsTextField.kt`
- `FriendsCard.kt`
- `FriendsScreenLayout.kt`