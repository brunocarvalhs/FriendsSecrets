# Friends Secrets

![Friends Secrets Logo](app/src/main/ic_launcher-playstore.png) <!-- Logo do projeto -->

## Descrição

**Friends Secrets** é um aplicativo Android projetado para facilitar a troca de segredos e mensagens de forma anônima entre amigos. O aplicativo combina interações divertidas com uma interface intuitiva, permitindo que os usuários compartilhem segredos sem revelar suas identidades. Com uma arquitetura modular, o projeto é altamente escalável e manutenível.

### Objetivos do Projeto

- Proporcionar um ambiente seguro e divertido para a troca de segredos.
- Facilitar a interação social entre amigos de maneira anônima.
- Implementar uma arquitetura robusta que suporte futuras expansões e novas funcionalidades.

## Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **Framework**: Android Jetpack (incluindo LiveData, ViewModel e Navigation)
- **Banco de Dados**: Room
- **Comunicação**: Retrofit (para chamadas de API)
- **Notificações**: Firebase Cloud Messaging (FCM)
- **Testes**: JUnit, Espresso, Mockito
- **Bibliotecas Adicionais**:
   - Generative AI
   - Lottie para animações
   - Coil para carregamento de imagens

## Requisitos

- **Android Studio**: Versão 4.0 ou superior
- **SDK Android**: API 21 (Lollipop) ou superior
- **Java Development Kit (JDK)**: JDK 11 ou superior

## Instalação

Siga as instruções abaixo para instalar o projeto localmente:

### Clonando o Repositório

1. Abra o terminal ou prompt de comando.
2. Execute o seguinte comando para clonar o repositório:

```bash
git clone https://github.com/seuusuario/friends-secrets.git
```

3. Navegue até o diretório do projeto:

```bash
cd friends-secrets
```

### Configurando o Ambiente

1. Abra o projeto no Android Studio.
2. Sincronize as dependências do Gradle. Clique em Sync Now quando solicitado.
3. Configure as credenciais da API (se necessário). Adicione suas chaves de API no arquivo local.properties ou conforme a documentação da API.

### Executando o Aplicativo

1. Conecte um dispositivo Android ou inicie um emulador.
2. Execute o aplicativo clicando no botão Run no Android Studio.

### Uso

Após a instalação, siga estas etapas para começar a usar o aplicativo:

1. Registro/Login: Crie uma nova conta ou faça login com uma conta existente.
2. Navegação: Utilize a barra de navegação para acessar diferentes seções do aplicativo, como:
   - Home: Visualize segredos e mensagens.
   - Grupos: Crie e gerencie grupos para compartilhar segredos.
   - Configurações: Ajuste as preferências do aplicativo.
3. Compartilhamento de Segredos: Use a opção de compartilhar segredos de forma anônima.

### Funcionalidades

- Criação de Conta: Cadastro fácil com email e senha. 
- Compartilhamento Anônimo: Envie segredos que podem ser visualizados por amigos. 
- Grupos: Crie grupos para discussões específicas. 
- Notificações: Receba notificações em tempo real quando novos segredos forem enviados. 
- Tema Personalizável: Altere o tema do aplicativo conforme sua preferência. 
- Suporte a Biometria: Utilize autenticação por impressão digital ou reconhecimento facial.

### Arquitetura do Projeto

O projeto segue uma arquitetura em camadas, promovendo a separação de responsabilidades e facilitando a manutenção. A arquitetura é composta pelas seguintes camadas:

1. Camada de Apresentação 
   - Activities e Fragments: Interface do usuário. 
   - ViewModels: Lógica de apresentação. 
2. Camada de Domínio 
   - Entidades: Modelos de domínio. 
   - Repositórios: Interfaces para operações de dados. 
   - Casos de Uso: Lógica de negócio específica.
3. Camada de Dados 
   - API Remota: Integração com serviços externos. 
   - Banco de Dados Local: Persistência de dados.

### Diagrama de Arquitetura

+---------------------+
|   Camada de         |
|   Apresentação      |
|  +---------------+  |
|  |   Activity    |  |
|  +---------------+  |
|  |   Fragment    |  |
|  +---------------+  |
|  |   ViewModel   |  |
|  +---------------+  |
+---------|-----------+
|
V
+---------------------+
|   Camada de         |
|   Domínio           |
|  +---------------+  |
|  |  Entidades    |  |
|  +---------------+  |
|  | Repositórios  |  |
|  +---------------+  |
|  | Casos de Uso  |  |
|  +---------------+  |
+---------|-----------+
|
V
+---------------------+
|   Camada de         |
|   Dados             |
|  +---------------+  |
|  | API Remota    |  |
|  +---------------+  |
|  | Banco de Dados |  |
|  +---------------+  |
+---------------------+

### Requisitos

1. [Requisitos Funcionais](./docs/functional-requirements.md)
2. [Requisitos Não Funcionais](./docs/non-functional-requirements.md)

## Testes

Os testes são uma parte essencial do desenvolvimento. O projeto inclui:
- Testes Unitários: Utilizando JUnit e Mockito para garantir que cada unidade de código funcione como esperado.
- Testes de Instrumentação: Utilizando Espresso para testar a interface do usuário em dispositivos reais.

### Executando os Testes

Para executar os testes, siga os passos:

1. Abra o terminal no diretório do projeto.
2. Execute o seguinte comando:

```bash
./gradlew test
```

Para testes de instrumentação, use:

```bash
./gradlew connectedAndroidTest
```

## Plugins e Dependências

O projeto utiliza os seguintes plugins e dependências:

### Plugins
- Android Application: Para desenvolvimento de aplicativos Android.
- Kotlin: Suporte a Kotlin.
- Kotlin Compose: Para desenvolvimento de interfaces de usuário com Jetpack Compose.
- Firebase: Para serviços de backend, como Analytics, Crashlytics e Firestore.
- Dokka: Para documentação do código.

## Contribuição

Contribuições são bem-vindas! Se você deseja contribuir com o projeto, siga estas etapas:

1. Fork o repositório.
2. Crie uma nova branch para suas alterações (git checkout -b feature/nome-da-feature).
3. Faça suas alterações e commit (git commit -m 'Adiciona nova funcionalidade').
4. Envie suas alterações (git push origin feature/nome-da-feature).
5. Abra um Pull Request.

## Licença

Este projeto é licenciado sob a [MIT License]().
