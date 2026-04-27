# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [3.1.0] - 2026-04-27

### Adicionado
- Suporte a novos idiomas: Francês (fr), Alemão (de), Italiano (it), Holandês (nl) e Polonês (pl).
- Internacionalização completa dos módulos de Biometria, Chat, Criação e Detalhes de Grupos, Sorteio e Configurações.

### Melhorado
- Atualização e sincronização das traduções em Espanhol (es) e Português (pt-BR) para paridade com o idioma principal.

## [3.0.0] - 2025-05-22

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
