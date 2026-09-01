# Friends Secrets

<div align="center">
  <img src="app/src/main/ic_launcher-playstore.png" alt="Friends Secrets Logo" width="200"/>
  <h3>Amigo Secreto 2.0: Mistério, Diversão e Conexão Anônima</h3>
  <p><i>Transforme a troca de segredos e presentes em uma experiência digital inesquecível.</i></p>
</div>

[![GitHub release (latest by date)](https://img.shields.io/github/v/release/brunocarvalhs/FriendsSecrets)](https://github.com/brunocarvalhs/FriendsSecrets/releases)
[![License](https://img.shields.io/github/license/brunocarvalhs/FriendsSecrets)](LICENSE)

## 📋 O que é o Friends Secrets?

O **Friends Secrets** não é apenas mais um app de mensagens; é o seu novo aliado para organizar Amigos Secretos e interagir com seus círculos de confiança de forma totalmente anônima. 

Sabe aquele mistério de quem tirou quem? Ou aquela vontade de enviar um elogio (ou uma brincadeira) sem se identificar? O Friends Secrets torna isso digital, seguro e extremamente divertido.

### 🎯 Por que usar?

- **Amigo Secreto Sem Papel**: Crie grupos, convide amigos e faça o sorteio instantaneamente. Nada de papéis perdidos ou sorteios repetidos.
- **Anonimato Seguro**: Envie mensagens e dicas anônimas dentro dos seus grupos. O mistério é garantido, mas a segurança também.
- **Consultoria de IA**: Não sabe o que dar de presente? Nossa IA integrada analisa o perfil e dá sugestões criativas para você não errar no presente.
- **Privacidade em Primeiro Lugar**: Autenticação biométrica e segurança Firebase para que seus segredos continuem sendo... segredos.

## ✨ Funcionalidades Principais

- **🎭 Mistério Garantido**: Envio de mensagens anônimas e sistema de dicas para apimentar o Amigo Secreto.
- **🎲 Sorteio Inteligente**: Algoritmo de sorteio automático com celebração animada e notificações instantâneas para todos os membros.
- **🤖 IA Amiga**: Chat generativo com reações a mensagens para sugestões de presentes e ideias de surpresas.
- **🔗 Convites Flexíveis**: Convide por link, deep link (`friendssecrets://join?code=`), QR Code ou cartão de convite ilustrado para compartilhar.
- **🎁 Lista de Desejos**: Cada membro monta sua wishlist, com preview rico para itens que são links de produtos.
- **⏰ Lembretes**: Notificação local com a data do sorteio/troca de presentes.
- **🔐 Blindagem Digital**: Login via telefone, biometria e exclusão total de dados sob demanda.
- **🌎 Multilíngue**: Interface disponível em Português (BR), Inglês, Espanhol, Francês, Alemão, Holandês, Polonês e Coreano.
- **🎨 Experiência Premium**: Interface moderna com suporte a Material You (Tema Dinâmico) e animações fluidas.

- **Autenticação Segura**: 
  - Login com número de telefone
  - Autenticação biométrica (impressão digital/reconhecimento facial)
  - Perfil personalizável com foto

- **Gerenciamento de Grupos**: 
  - Criação e edição de grupos para amigos secretos
  - Visualização detalhada de membros do grupo, com opção de remover participantes
  - Convite via link, deep link, QR Code ou cartão de convite compartilhável
  - Sorteio automático de amigos secretos, com celebração animada
  - Lembrete local para a data da troca de presentes

- **Compartilhamento Anônimo**: 
  - Envio de mensagens anônimas
  - Visualização de segredos compartilhados

- **Lista de Desejos**:
  - Cada membro cria e edita sua própria wishlist
  - Preview automático para itens que são links de produtos
  - Compartilhamento da wishlist com o grupo

- **Chat com IA**: 
  - Interação com inteligência artificial
  - Reações a mensagens
  - Sugestões de presentes e ideias

- **Notificações Push**:
  - Aviso quando o sorteio é concluído
  - Aviso de novas mensagens no chat

- **Personalização**:
  - Temas claro e escuro
  - Suporte a 8 idiomas
  - Configurações de aparência personalizáveis

- **Segurança**:
  - Opção de deletar conta
  - Proteção de dados sensíveis

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Arquitetura**: Clean Architecture + MVVM + MVI Pattern
- **Injeção de Dependência**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
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
  - [ZXing](https://github.com/zxing/zxing) (Geração e leitura de QR Code para convites)
  - [WorkManager](https://developer.android.com/jetpack/androidx/releases/work) (Sincronização de notificações push em segundo plano)
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

- **Android Studio**: Ladybug (2024.2.1) ou superior
- **SDK Android**: mínimo API 26 (Android 8.0 Oreo), compilado com API 37
- **Java Development Kit (JDK)**: JDK 17 (obrigatório)
- **Gradle**: gerenciado pelo wrapper (via AGP 9.x)

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

O projeto utiliza uma estrutura modular para garantir escalabilidade e separação de interesses:

```
├── app/                   # Módulo principal que orquestra a aplicação
├── baselineprofile/       # Perfil de baseline para otimização de startup
├── core/                  # Módulos compartilhados entre as features
│   ├── analytics/         # Rastreamento de eventos
│   ├── biometric/         # Abstração de biometria
│   ├── deviceid/          # Identificação de dispositivo
│   ├── domain/            # Entidades e utilitários globais
│   ├── logger/            # Abstração de logging
│   ├── navigation/        # Hub de navegação centralizado
│   ├── network/           # Configuração de rede e Firebase
│   ├── notifications/     # Notificações push
│   ├── remote/            # Remote Config e feature flags
│   ├── security/          # Segurança e criptografia
│   ├── storage/           # Persistência local
│   ├── ui/                # Design System e componentes comuns
│   └── ...
└── features/              # Módulos de funcionalidades específicas
    ├── biometric/          # Fluxo de autenticação biométrica
    ├── chat/               # Chat com IA
    ├── group/              # Sub-módulos de gerenciamento de grupos
    │   ├── list/
    │   ├── details/
    │   ├── create/
    │   └── draw/           # Sorteio de amigo secreto
    └── settings/           # Configurações do app
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

- [Visão Geral do Projeto](./docs/PROJECT_OVERVIEW.md)
- [Arquitetura](./docs/ARCHITECTURE.md)
- [Guia de Instalação e Configuração](./docs/SETUP.md)
- [Melhores Práticas de Desenvolvimento](./docs/BEST_PRACTICES.md)
- [Requisitos Funcionais](./docs/functional-requirements.md)
- [Requisitos Não Funcionais](./docs/non-functional-requirements.md)
- [Casos de Uso](./docs/use-cases.md)
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
