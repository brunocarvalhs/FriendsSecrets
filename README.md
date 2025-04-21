# Friends Secrets

<div align="center">
  <img src="app/src/main/ic_launcher-playstore.png" alt="Friends Secrets Logo" width="200"/>
  <h3>Compartilhe segredos de forma anônima com seus amigos</h3>
</div>

[![GitHub release (latest by date)](https://img.shields.io/github/v/release/brunocarvalhs/FriendsSecrets)](https://github.com/brunocarvalhs/FriendsSecrets/releases)
[![License](https://img.shields.io/github/license/brunocarvalhs/FriendsSecrets)](LICENSE)

## 📋 Descrição

**Friends Secrets** é um aplicativo Android projetado para facilitar a troca de segredos e mensagens de forma anônima entre amigos. O aplicativo combina interações divertidas com uma interface intuitiva, permitindo que os usuários compartilhem segredos sem revelar suas identidades. Com uma arquitetura modular baseada em Clean Architecture e MVVM, o projeto é altamente escalável e manutenível.

### 🎯 Objetivos do Projeto

- Proporcionar um ambiente seguro e divertido para a troca de segredos
- Facilitar a interação social entre amigos de maneira anônima
- Implementar uma arquitetura robusta que suporte futuras expansões e novas funcionalidades
- Oferecer uma experiência de usuário intuitiva e agradável

## ✨ Funcionalidades

- **Autenticação Segura**: 
  - Login com número de telefone
  - Autenticação biométrica (impressão digital/reconhecimento facial)
  - Perfil personalizável com foto

- **Gerenciamento de Grupos**: 
  - Criação e edição de grupos para amigos secretos
  - Visualização detalhada de membros do grupo
  - Sorteio automático de amigos secretos

- **Compartilhamento Anônimo**: 
  - Envio de mensagens anônimas
  - Visualização de segredos compartilhados

- **Chat com IA**: 
  - Interação com inteligência artificial
  - Sugestões de presentes e ideias

- **Personalização**:
  - Temas claro e escuro
  - Configurações de aparência personalizáveis

- **Segurança**:
  - Opção de deletar conta
  - Proteção de dados sensíveis

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Arquitetura**: Clean Architecture + MVVM
- **Firebase**:
  - [Firestore](https://firebase.google.com/docs/firestore) (Banco de dados)
  - [Authentication](https://firebase.google.com/docs/auth) (Autenticação)
  - [Crashlytics](https://firebase.google.com/docs/crashlytics) (Monitoramento de erros)
  - [Remote Config](https://firebase.google.com/docs/remote-config) (Configurações remotas)
  - [Analytics](https://firebase.google.com/docs/analytics) (Análise de uso)
- **Bibliotecas**:
  - [Google Generative AI](https://ai.google.dev/) (Chat com IA)
  - [Lottie](https://airbnb.design/lottie/) (Animações)
  - [Coil](https://coil-kt.github.io/coil/) (Carregamento de imagens)
  - [Biometric](https://developer.android.com/jetpack/androidx/releases/biometric) (Autenticação biométrica)
  - [uCrop](https://github.com/Yalantis/uCrop) (Recorte de imagens)
- **Testes**:
  - [JUnit](https://junit.org/junit4/)
  - [Mockito](https://site.mockito.org/)
  - [Espresso](https://developer.android.com/training/testing/espresso)
  - [Robolectric](http://robolectric.org/)
  - [MockK](https://mockk.io/)

## 📱 Capturas de Tela

*Capturas de tela serão adicionadas em breve*

## 🚀 Instalação

### Requisitos

- **Android Studio**: Arctic Fox (2020.3.1) ou superior
- **SDK Android**: API 24 (Android 7.0 Nougat) ou superior
- **Java Development Kit (JDK)**: JDK 11 ou superior
- **Gradle**: 7.0 ou superior

### Configuração do Ambiente de Desenvolvimento

1. Clone o repositório:
   ```bash
   git clone https://github.com/brunocarvalhs/FriendsSecrets.git
   cd FriendsSecrets
   ```

2. Abra o projeto no Android Studio.

3. Configure as variáveis de ambiente necessárias:
   - `API_KEY`: Chave da API do Google Generative AI
   - `KEYSTORE_PASSWORD`, `KEYSTORE_ALIAS`, `KEY_PASSWORD`: Para builds de release

4. Sincronize o projeto com os arquivos Gradle.

5. Execute o aplicativo em um emulador ou dispositivo físico.

## 🏗️ Arquitetura

O projeto segue os princípios de **Clean Architecture** combinados com o padrão **MVVM** (Model-View-ViewModel), organizados em três camadas principais:

### 1. Camada de Apresentação (Presentation Layer)
- **Views**: Activities, Fragments e componentes Compose
- **ViewModels**: Gerenciam o estado da UI e a lógica de apresentação
- **UI Components**: Componentes reutilizáveis da interface

### 2. Camada de Domínio (Domain Layer)
- **Entidades**: Modelos de domínio
- **Casos de Uso**: Encapsulam a lógica de negócios
- **Repositórios (Interfaces)**: Definem contratos para acesso a dados

### 3. Camada de Dados (Data Layer)
- **Repositórios (Implementações)**: Implementam interfaces da camada de domínio
- **Fontes de Dados**: Remotas (Firebase) e locais
- **Modelos de Dados**: Representações dos dados para persistência

### Diagrama de Arquitetura

```
┌─────────────────────────────────────────────────────┐
│                  Presentation Layer                  │
│                                                     │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────┐  │
│  │   Activity  │    │   Fragment  │    │ Compose │  │
│  └──────┬──────┘    └──────┬──────┘    └────┬────┘  │
│         │                  │                │       │
│         └──────────┬───────┴────────┬──────┘       │
│                    │                │              │
│            ┌───────▼────────┐       │              │
│            │    ViewModel   │◄──────┘              │
│            └───────┬────────┘                      │
└────────────────────┼──────────────────────────────┘
                     │
┌────────────────────▼──────────────────────────────┐
│                   Domain Layer                     │
│                                                   │
│  ┌─────────────┐    ┌─────────────┐              │
│  │  Use Cases  │    │  Entities   │              │
│  └──────┬──────┘    └─────────────┘              │
│         │                                        │
│  ┌──────▼──────┐                                 │
│  │ Repositories│ (Interfaces)                    │
│  └──────┬──────┘                                 │
└─────────┼───────────────────────────────────────┘
          │
┌─────────▼───────────────────────────────────────┐
│                   Data Layer                     │
│                                                 │
│  ┌─────────────┐    ┌─────────────────────┐     │
│  │ Repositories│    │     Data Sources    │     │
│  │(Implementation)  │  (Remote & Local)   │     │
│  └──────┬──────┘    └──────────┬──────────┘     │
│         │                      │                │
│         └──────────┬───────────┘                │
│                    │                            │
│            ┌───────▼────────┐                   │
│            │  Data Models   │                   │
│            └────────────────┘                   │
└───────────────────────────────────────────────┘
```

## 📊 Estrutura do Projeto

```
app/
├── src/
│   ├── main/
│   │   ├── java/br/com/brunocarvalhs/friendssecrets/
│   │   │   ├── commons/           # Utilitários e componentes comuns
│   │   │   │   ├── analytics/     # Rastreamento de eventos
│   │   │   │   ├── extensions/    # Extensões Kotlin
│   │   │   │   ├── initialization/# Inicialização de SDKs
│   │   │   │   ├── logger/        # Sistema de logs
│   │   │   │   ├── navigation/    # Navegação entre telas
│   │   │   │   ├── remote/        # Configurações remotas
│   │   │   │   ├── security/      # Segurança e criptografia
│   │   │   │   └── theme/         # Temas e estilos
│   │   │   │
│   │   │   ├── data/              # Camada de dados
│   │   │   │   ├── exceptions/    # Exceções personalizadas
│   │   │   │   ├── manager/       # Gerenciadores de dados
│   │   │   │   ├── model/         # Modelos de dados
│   │   │   │   ├── repository/    # Implementações de repositórios
│   │   │   │   └── service/       # Serviços de API
│   │   │   │
│   │   │   ├── domain/            # Camada de domínio
│   │   │   │   ├── entities/      # Entidades de domínio
│   │   │   │   ├── repository/    # Interfaces de repositório
│   │   │   │   └── useCases/      # Casos de uso
│   │   │   │
│   │   │   └── presentation/      # Camada de apresentação
│   │   │       ├── ui/            # Componentes de UI reutilizáveis
│   │   │       └── views/         # Telas do aplicativo
│   │   │           ├── auth/      # Autenticação
│   │   │           ├── generative/# Chat com IA
│   │   │           ├── group/     # Gerenciamento de grupos
│   │   │           ├── home/      # Tela principal
│   │   │           └── settings/  # Configurações
│   │   │
│   │   └── res/                   # Recursos (layouts, strings, etc.)
│   │
│   ├── androidTest/               # Testes de instrumentação
│   └── test/                      # Testes unitários
│
├── build.gradle.kts               # Configuração do Gradle
└── proguard-rules.pro             # Regras ProGuard
```

## 🧪 Testes

O projeto utiliza uma abordagem abrangente de testes:

### Testes Unitários
Localizados em `app/src/test/`, testam componentes individuais isoladamente.

```bash
./gradlew test
```

### Testes de Instrumentação
Localizados em `app/src/androidTest/`, testam a interface do usuário e integrações.

```bash
./gradlew connectedAndroidTest
```

## 📝 Documentação Adicional

- [Requisitos Funcionais](./docs/functional-requirements.md)
- [Requisitos Não Funcionais](./docs/non-functional-requirements.md)
- [Política de Privacidade](./docs/PrivacyPolicy.md)
- [Termos e Condições](./docs/TermsEndConditions.md)
- [Changelog](./CHANGELOG.md)

## 🤝 Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Fork o repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Implemente suas mudanças e adicione testes quando possível
4. Commit suas alterações (`git commit -m 'feat: adiciona nova funcionalidade'`)
5. Push para a branch (`git push origin feature/nova-funcionalidade`)
6. Abra um Pull Request

Por favor, siga as [diretrizes de contribuição](CONTRIBUTING.md) e o [código de conduta](CODE_OF_CONDUCT.md).

## 📄 Licença

Este projeto é licenciado sob a [MIT License](LICENSE).

## 👨‍💻 Autor

**Bruno Carvalho** - [brunocarvalhs](https://github.com/brunocarvalhs)

---

<div align="center">
  <sub>Feito com ❤️ por Bruno Carvalho</sub>
</div>
