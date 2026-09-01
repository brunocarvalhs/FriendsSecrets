# Changelog

## [3.6.2] - 2026-09-01

* Merge pull request #79 from brunocarvalhs/fix/wishlist-share-errors-and-ai-context
* fix(group-details,chat): surface action errors and use member likes for AI suggestions
* chore: bump version to 3.6.1 [skip ci]


## [3.6.1] - 2026-09-01

* Merge pull request #78 from brunocarvalhs/fix/app-check-for-ai-logic
* fix(app): initialize Firebase App Check so AI Logic requests aren't blocked
* chore: bump version to 3.6.0 [skip ci]


## [3.6.0] - 2026-09-01

* Merge pull request #77 from brunocarvalhs/feat/ai-gift-chat
* fix(chat): wrap analytics call in AiGiftChatViewModel to satisfy MaxLineLength
* fix(chat): accept a Modifier in AI chat composables
* feat(chat): add AI gift-suggestion chat using Firebase AI Logic
* chore: bump version to 3.5.0 [skip ci]


## [3.5.0] - 2026-09-01

* Merge pull request #76 from brunocarvalhs/feat/member-adjectives
* Merge remote-tracking branch 'origin/develop' into feat/member-adjectives
* chore: bump version to 3.4.0 [skip ci]
* feat(group-details): let group members add adjectives to each other


## [3.4.0] - 2026-09-01

* Merge pull request #75 from brunocarvalhs/feat/group-invite-bottom-sheet
* Merge pull request #74 from brunocarvalhs/fix/chat-whatsapp-style-ui
* fix(group-details): translate invite bottom sheet strings to all locales
* fix(chat): translate date divider strings to all supported locales
* feat(group-details): consolidate invite actions into a bottom sheet
* fix(chat): keep input above the navigation bar and add WhatsApp-style date dividers
* chore: bump version to 3.3.1 [skip ci]


## [3.3.1] - 2026-09-01

* Merge pull request #73 from brunocarvalhs/fix/chat-local-history-cache
* fix(chat): persist chat history locally and stop auto-deleting it
* chore: bump version to 3.3.0 [skip ci]


## [3.3.0] - 2026-09-01

* ### Summary This update synchronizes the project documentation with version 3.2.2, reflecting a significant architectural expansion and updated development requirements. Key changes include the addition of several core and feature modules, updated system prerequisites (JDK 17, API 26+), and a finalized feature set including wishlists, AI-driven interactions, and multi-language support.
* Merge remote-tracking branch 'origin/develop' into develop
* chore: bump version to 3.2.2 [skip ci]
* feat(analytics): add chat engagement funnel and creator/joiner retention cohorts
* feat(analytics): add named funnel events for GA4 funnel exploration
* feat(deeplink): add friendssecrets://join?code= deep link and refresh Maestro regression suite
* fix(chat): avoid double setPersistenceEnabled crash and add Android 17 predictive back support


## [3.2.2] - 2026-09-01

* Rename README.md to CONFIG.md
* chore: bump version to 3.2.1 [skip ci]


## [3.2.1] - 2026-08-31

* Merge pull request #72 from brunocarvalhs/fix/group-details-scroll-and-qr-join
* fix(group-details): scrollable action cards + QR scan to join a group
* chore: bump version to 3.2.0 [skip ci]


## [3.2.0] - 2026-08-31

* Merge pull request #71 from brunocarvalhs/feat/add-fr-de-nl-pl-locales
* feat(i18n): add French, German, Dutch and Polish translations
* chore: bump version to 3.1.0 [skip ci]


## [3.1.0] - 2026-08-31

* Merge pull request #70 from brunocarvalhs/fix/develop-version-bump
* Merge pull request #69 from brunocarvalhs/perf/r8-optimization
* fix(ci): grant contents:write to the jobs that push version bumps
* fix(ci): bump version automatically on every push to develop
* perf(r8): remove redundant blanket keeps so R8 actually shrinks/obfuscates
* fix(ci): simplify Firebase App Distribution and stop matrix cancellation
* fix(ci): correct notifications flag handling in Firebase App Distribution step
* Merge pull request #67 from brunocarvalhs/feat/push-notifications
* refactor(push-notifications): replace Cloud Functions push with client-side WorkManager sync
* Merge remote-tracking branch 'origin/develop' into feat/push-notifications
* Merge pull request #66 from brunocarvalhs/feat/wishlist-product-link
* Merge pull request #65 from brunocarvalhs/feat/draw-celebration-animation
* Merge pull request #64 from brunocarvalhs/feat/group-invite-card
* Merge remote-tracking branch 'origin/develop' into feat/group-invite-card
* Merge pull request #63 from brunocarvalhs/feat/group-qr-invite
* Merge remote-tracking branch 'origin/develop' into feat/group-qr-invite
* Merge pull request #62 from brunocarvalhs/feat/group-date-reminder
* Merge remote-tracking branch 'origin/develop' into feat/group-date-reminder
* Merge pull request #61 from brunocarvalhs/feat/group-remove-member
* Merge remote-tracking branch 'origin/develop' into feat/group-remove-member
* Merge pull request #60 from brunocarvalhs/feat/chat-message-reactions
* Merge pull request #59 from brunocarvalhs/feat/share-wishlist
* Merge remote-tracking branch 'origin/develop' into feat/share-wishlist
* Merge pull request #58 from brunocarvalhs/feat/member-wishlist-edit
* fix(notifications): correct manifest reference to AppFirebaseMessagingService
* fix(group-details): extract magic numbers in GroupInviteCardRenderer
* fix(group-details): satisfy CI lint and detekt for the reminder feature
* fix(chat): satisfy detekt ComposableParamOrder in ChatMessageItem
* fix(group-details): satisfy detekt ComposableParamOrder in GroupDetailsContent
* feat: push notifications for draw completion and new chat messages
* feat(group-details): rich preview for wishlist items that are product links
* feat(group-draw): confetti celebration when the draw completes
* feat(group-details): shareable branded invite card image
* feat(group-details): share group invite as a QR code
* feat(group-details): add a local reminder for the draw date
* feat(group-details): allow the group owner to remove a member
* feat(chat): add quick emoji reactions to anonymous chat messages
* feat(group-details): let members share their wishlist as text
* feat(group-details): allow members to edit their own wishlist
* ci: create spec-driven develop
* ci: create spec-driven develop
* ### Summary This update introduces a comprehensive suite of engineering standards and "skills" documentation to guide Android development. These definitions establish formal patterns for architecture, UI, dependency injection, and data management, serving as a source of truth for both developers and AI agents.
* ci: refactor infrastructure and centralize pipeline configuration
* ci: use Service Account secrets for Firebase App Distribution
* ci: migrate Firebase App Distribution to Service Account authentication
* Feat/update kotlin (#57)
* build: update build tools and core dependencies (#56)
* fix: change targetSdk version to 37 (#53)
* docs: adicionar documentação técnica e base de conhecimento para habilidades de agente (#52)
* release: versão 3.0.1 (#50)
* feat: implementando privacy policy (#49)
* ci: simplificar validação de Keystore no workflow do GitHub Actions
* ci: ajustar decodificação da keystore e validação de alias no workflow
* ci: aprimorar workflow de build e validação de keystore
* build: atualizar versões de GitHub Actions e configurar Node.js 24
* fix: ajuste de ofuscação dos modulo (#47)
* build: resolver conflitos de merge e limpar arquivos de build nos módulos de features
* Merge branch 'master' into develop
* refactor: removendo código legado
* Merge branch 'master' into develop
* ci: ajustar workflow de deploy de key
* ci: ajustar workflow de deploy para Firebase App Distribution
* ci: ajustar workflow de deploy para Firebase App Distribution
* ci: ajustar workflow de deploy para Firebase App Distribution
* ci: ajustar workflow de deploy para Firebase App Distribution
* ci: ajustar workflow de deploy para Firebase App Distribution
* ci: ajustar workflow de deploy para Firebase App Distribution
* ci: ajustar workflow de deploy para Firebase App Distribution
* refactor: ajuste de pipeline para suporte a pull_request e push (#42)
* feat: refatoração de aplicativo removendo fluxo de login e mudando arquitetura (#41)
* Rename README.md to TUTORIAL.md
* feat: implementando detekt nos modulos
* feat: implementando detekt para pipeline
* feat: atualizar pipeline de build
* Merge branch 'master' into develop
* Versão 2.2.0
* feat: tela de criação de grupo (#39)
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* Update release-pr.yml
* ci: criando fluxo novo de release e revert (#38)
* feat:import do AddTrace
* Merge branch 'master' into develop
* fix: incluindo activity do ucrop no manifest (#35)
* feat: incluindo trace em cada método para ter dados de performance de cada método (#36)
* Merge branch 'master' into develop
* fix: correção de ui state no login (#34)
* feat: trace performance (#32)
* feat: add maestro tests for app flows (#30)
* Merge branch 'master' into develop
* feat: atualizando google-services (#28)
* Update CHANGELOG.md (#26)
* docs: atualização dos requisitos do projeto (#22)
* docs: melhora a documentação do projeto (#21)
* feat: ajuste de ambiente (#20)
* Update of homolog result (#19)
* feat: refatoração do código (#18)
* fix: Incluindo tratamento de botão não usado para evitar report da google (#17)
* feat: fluxo de login e gerenciamento de perfils (#16)
* feat: aumento de cobertura de testes (#15)


Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [3.0.2] - 2026-08-20

### Adicionado
- Inicialização da infraestrutura de **Spec-Driven Development (SDD)** com diretório `.specs/`.
- Registro de Decisões Arquiteturais (ADRs) iniciais no `STATE.md` (AD-001 a AD-005).
- Especificação de Baseline Estrutural validada via metodologia EARS.

### Melhorado
- Atualização completa da documentação técnica (`README.md` e `ARCHITECTURE.md`) para refletir a arquitetura multi-módulo real e o uso de **Dagger Hilt**.
- Sincronização do mapeamento de pacotes e UseCases entre os documentos e o código-fonte.

## [3.0.1] - 2026-05-02

### Adicionado
- Internacionalização completa do componente de visualização de conteúdo (WebView) com suporte a Inglês, Português, Espanhol e Coreano.
- Configuração de Keystore e automação de deploy para **Firebase App Distribution** (suporte a builds de Debug e Release).
- Sistema de governança de dependências no CI via Danger para bloquear bibliotecas obsoletas ou com vulnerabilidades.

### Melhorado
- Padronização de nomes de parâmetros lambda em componentes Compose (`WebViewContainer`) para o tempo presente, seguindo as melhores práticas e regras de Lint.
- Scripts do **Danger CI** totalmente refatorados:
    - Relatórios de arquivos modificados agora são agrupados por módulo.
    - Verificação obrigatória de testes unitários para PRs do tipo `feat` e `fix`.
    - Validação de conformidade com **Conventional Commits** nos títulos de PR.
- Otimização do pipeline de CI para suportar assinaturas de APK e upload de múltiplos artefatos.
- Atualização do **Template de Pull Request** para padronizar as informações de review e garantia de qualidade.

### Corrigido
- Ajuste crítico nas regras do **ProGuard/R8** para evitar quebras na Injeção de Dependências (Hilt) e na inicialização de módulos (`FeatureInitializer`) em builds de produção (Release).
- Desativada a minificação individual no módulo `:features:group:list` para garantir a estabilidade do código ofuscado.

## [3.0.0] - 2026-04-26

### Adicionado
- Nova arquitetura modular
- Suporte a Temas Dinâmicos (Material You)
- Gerenciamento de preferências centralizado

### Melhorado
- Migração completa para Version Catalog (libs.versions.toml)
- Atualização para Android 15 (SDK 35/37)
- Otimização de performance no carregamento inicial

## [2.2.1] - 2025-05-22

### Adicionado
- Centralização de configurações de SDK e versionamento no Version Catalog

### Melhorado
- Atualização do Version Name para 2.2.1
- Sincronização de versões entre módulos

## [1.2.4] - 2025-04-26
- Atualizando variável da CI/CD do google-services

## [1.2.3] - 2025-04-24

### Adicionado
- Melhorias de desempenho e estabilidade
- Otimização do uso de recursos
- Incluindo login no processo de identificação do cliente
- Leitura de contato para facilitar o processo de criação de amigo secreto

## [1.2.2] - 2025-03-11

### Corrigido
- Tratamento de botão não utilizado para evitar report da Google Play Store
- Correção de problemas de compatibilidade em dispositivos mais antigos

## [1.2.1] - 2025-02-15

### Adicionado
- Funcionalidade de recorte de imagem para foto de perfil
- Validação para o campo de nome no formulário de perfil

### Melhorado
- Fluxo de edição de perfil otimizado
- Ajustes no layout do formulário de perfil para melhor usabilidade

### Corrigido
- Bugs no carregamento de imagem de perfil
- Problemas de validação em formulários

## [1.2.0] - 2025-01-30

### Adicionado
- Opção de deletar conta
- Opção de editar nome de usuário
- Fluxo completo de edição de perfil
- Cadastro de perfil com foto
- Login com número de telefone
- Integração com biblioteca uCrop para manipulação de imagens

### Melhorado
- Segurança no processo de autenticação
- Interface de gerenciamento de perfil

## [1.1.2] - 2025-03-11

### Corrigido
- Report da Google Play Store sobre botão sem evento associado
- Problemas de estabilidade em dispositivos específicos

## [1.1.1] - 2025-01-16

### Adicionado
- Funcionalidades MVP (Produto Mínimo Viável)
- Fluxo de tour guiado para novos usuários
- Fluxo de lista de amigos secretos
- Cadastro e gerenciamento de grupos
- Chat com IA usando Google Generative AI
- Sistema de toggles integrado com Firebase Remote Config
- Modelos de base de dados com Firebase Firestore
- Configuração do Firebase Crashlytics para monitoramento de erros
- Fluxo de configuração com tema customizado (claro/escuro)

### Melhorado
- Experiência de usuário com animações Lottie
- Performance geral do aplicativo

## [1.1.0] - 2025-01-13

### Adicionado
- Implementação inicial de grupos de amigos secretos
- Integração com Firebase Authentication
- Suporte a múltiplos idiomas (Português, Inglês, Espanhol e Coreano)
- Tema escuro

### Melhorado
- Interface de usuário com Material Design 3
- Navegação entre telas

## [1.0.0] - 2025-01-12

### Adicionado
- Versão inicial do aplicativo
- Estrutura base do projeto usando Clean Architecture
- Configuração inicial do Firebase
- Sistema de autenticação básico
- Interface de usuário com Jetpack Compose
