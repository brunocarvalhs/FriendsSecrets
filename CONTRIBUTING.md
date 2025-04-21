# Guia de Contribuição

Obrigado pelo interesse em contribuir com o projeto Friends Secrets! Este documento fornece diretrizes e instruções para contribuir com o desenvolvimento do aplicativo.

## Índice

- [Código de Conduta](#código-de-conduta)
- [Como Posso Contribuir?](#como-posso-contribuir)
  - [Reportando Bugs](#reportando-bugs)
  - [Sugerindo Melhorias](#sugerindo-melhorias)
  - [Contribuindo com Código](#contribuindo-com-código)
- [Estilo de Código](#estilo-de-código)
- [Processo de Pull Request](#processo-de-pull-request)
- [Ambiente de Desenvolvimento](#ambiente-de-desenvolvimento)

## Código de Conduta

Este projeto e todos os participantes estão sujeitos ao [Código de Conduta](CODE_OF_CONDUCT.md). Ao participar, espera-se que você mantenha este código. Por favor, reporte comportamentos inaceitáveis para [brunocarvalhs@gmail.com](mailto:brunocarvalhs@gmail.com).

## Como Posso Contribuir?

### Reportando Bugs

Esta seção orienta você sobre como reportar bugs. Seguir estas diretrizes ajuda os mantenedores a entender seu relatório, reproduzir o comportamento e encontrar relatórios relacionados.

Antes de criar um relatório de bug, verifique se o problema já não foi reportado buscando na [seção de Issues](https://github.com/brunocarvalhs/FriendsSecrets/issues) do projeto.

> **Nota:** Se você encontrar uma issue **Fechada** que parece ser igual ao problema que está enfrentando, abra uma nova issue e inclua um link para a issue original no corpo da sua nova issue.

#### Como Submeter um Bom Relatório de Bug

Bugs são rastreados como [issues no GitHub](https://github.com/brunocarvalhs/FriendsSecrets/issues). Crie uma issue e forneça as seguintes informações:

* **Use um título claro e descritivo** para identificar o problema.
* **Descreva os passos exatos para reproduzir o problema** com o máximo de detalhes possível.
* **Forneça exemplos específicos para demonstrar os passos**. Inclua links para arquivos, ou cópias de código, que você usou nesses exemplos.
* **Descreva o comportamento observado após seguir os passos** e aponte qual é exatamente o problema com esse comportamento.
* **Explique qual comportamento você esperava ver e por quê.**
* **Inclua capturas de tela** se possível.
* **Se o problema não foi desencadeado por uma ação específica**, descreva o que estava fazendo antes do problema acontecer.

Inclua detalhes sobre seu ambiente:

* **Qual a versão do aplicativo você está usando?**
* **Qual o nome e versão do dispositivo que você está usando?**
* **Qual a versão do Android que você está usando?**

### Sugerindo Melhorias

Esta seção orienta você sobre como submeter uma sugestão de melhoria, incluindo recursos completamente novos e pequenas melhorias na funcionalidade existente.

#### Como Submeter uma Boa Sugestão de Melhoria

As sugestões de melhoria são rastreadas como [issues no GitHub](https://github.com/brunocarvalhs/FriendsSecrets/issues). Crie uma issue e forneça as seguintes informações:

* **Use um título claro e descritivo** para a issue.
* **Forneça uma descrição passo a passo da melhoria sugerida** com o máximo de detalhes possível.
* **Forneça exemplos específicos para demonstrar os passos** ou aponte para recursos semelhantes em outros aplicativos.
* **Descreva o comportamento atual** e **explique qual comportamento você esperava ver** e por quê.
* **Inclua capturas de tela ou mockups** que ajudem a visualizar a melhoria.
* **Explique por que essa melhoria seria útil** para a maioria dos usuários do Friends Secrets.
* **Liste alguns outros aplicativos onde essa melhoria existe**, se aplicável.

### Contribuindo com Código

#### Configuração Local do Ambiente de Desenvolvimento

Para começar a desenvolver para o Friends Secrets, você precisará configurar seu ambiente:

1. Instale o [Android Studio](https://developer.android.com/studio)
2. Clone o repositório:
   ```bash
   git clone https://github.com/brunocarvalhs/FriendsSecrets.git
   ```
3. Abra o projeto no Android Studio
4. Configure as variáveis de ambiente necessárias (consulte o README.md)
5. Sincronize o projeto com os arquivos Gradle

#### Fluxo de Trabalho para Contribuições

1. Crie uma branch a partir de `develop`:
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/nome-da-sua-feature
   ```

2. Faça suas alterações seguindo as [diretrizes de estilo de código](#estilo-de-código)

3. Teste suas alterações:
   - Execute testes unitários: `./gradlew test`
   - Execute testes de instrumentação (se aplicável): `./gradlew connectedAndroidTest`
   - Teste manualmente no emulador ou dispositivo físico

4. Commit suas alterações seguindo as [convenções de commit](#convenções-de-commit):
   ```bash
   git add .
   git commit -m "feat: adiciona nova funcionalidade"
   ```

5. Push para sua branch:
   ```bash
   git push origin feature/nome-da-sua-feature
   ```

6. Abra um Pull Request para a branch `develop`

## Estilo de Código

### Convenções de Código Kotlin

- Siga o [Guia de Estilo Kotlin](https://developer.android.com/kotlin/style-guide) oficial
- Use a formatação padrão do Android Studio
- Mantenha os nomes de variáveis, funções e classes descritivos e em português
- Documente classes e funções públicas com comentários KDoc

### Convenções de Commit

Usamos [Conventional Commits](https://www.conventionalcommits.org/pt-br/) para mensagens de commit padronizadas:

```
<tipo>[escopo opcional]: <descrição>

[corpo opcional]

[rodapé(s) opcional(is)]
```

Tipos comuns:
- **feat**: Uma nova funcionalidade
- **fix**: Uma correção de bug
- **docs**: Alterações na documentação
- **style**: Alterações que não afetam o significado do código (espaços em branco, formatação, etc)
- **refactor**: Uma alteração de código que não corrige um bug nem adiciona um recurso
- **test**: Adicionando testes ausentes ou corrigindo testes existentes
- **chore**: Alterações no processo de build ou ferramentas auxiliares

Exemplo:
```
feat(auth): adiciona login com número de telefone

Implementa a funcionalidade de login usando número de telefone e
verificação por SMS através do Firebase Authentication.

Resolve: #123
```

## Processo de Pull Request

1. Siga todas as instruções em [o modelo de pull request](PULL_REQUEST_TEMPLATE.md)
2. Siga o [fluxo de trabalho para contribuições](#fluxo-de-trabalho-para-contribuições)
3. Após enviar seu pull request, verifique se todas as [verificações de status](https://help.github.com/articles/about-status-checks/) estão passando

Enquanto as verificações estiverem sendo executadas, é recomendável que você:
- Verifique se os testes estão passando
- Verifique se não há conflitos com a branch de destino
- Revise seu próprio código

Se um mantenedor solicitar mais alterações:
- Faça as alterações necessárias
- Faça push para sua branch
- O PR será atualizado automaticamente

## Ambiente de Desenvolvimento

### Estrutura do Projeto

Familiarize-se com a estrutura do projeto conforme descrito no [README.md](README.md#estrutura-do-projeto).

### Dependências

O projeto utiliza várias bibliotecas e frameworks. Consulte o arquivo `build.gradle.kts` para ver a lista completa de dependências.

### Testes

- Escreva testes unitários para toda nova funcionalidade
- Mantenha a cobertura de testes alta
- Use mocks quando apropriado para isolar unidades de código

---

Obrigado por contribuir com o Friends Secrets! Suas contribuições tornam a comunidade open source um lugar incrível para aprender, inspirar e criar.