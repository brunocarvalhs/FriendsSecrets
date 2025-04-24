# Guia de Instalação e Configuração

Este documento fornece instruções detalhadas para configurar o ambiente de desenvolvimento e executar o aplicativo Friends Secrets localmente.

## Requisitos de Sistema

### Software Necessário

- **Android Studio**: Arctic Fox (2020.3.1) ou superior
- **JDK (Java Development Kit)**: Versão 11 ou superior
- **Gradle**: Versão 7.0 ou superior (geralmente gerenciado pelo Android Studio)
- **Git**: Para controle de versão

### Hardware Recomendado

- **Processador**: Intel Core i5 ou equivalente (ou superior)
- **Memória RAM**: 8GB mínimo, 16GB recomendado
- **Espaço em Disco**: Pelo menos 10GB de espaço livre
- **Dispositivo Android ou Emulador**: 
  - Dispositivo físico com Android 7.0 (API 24) ou superior
  - Emulador configurado com Android 7.0 ou superior

## Configuração do Ambiente

### 1. Instalação do Android Studio

1. Baixe o Android Studio do [site oficial](https://developer.android.com/studio)
2. Siga as instruções de instalação para seu sistema operacional
3. Durante a instalação, certifique-se de incluir:
   - Android SDK
   - Android SDK Platform
   - Android Virtual Device (AVD)

### 2. Configuração do JDK

1. Verifique se você tem o JDK 11 ou superior instalado:
   ```bash
   java -version
   ```
2. Se não estiver instalado, baixe e instale o JDK da [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html) ou use o OpenJDK
3. Configure a variável de ambiente `JAVA_HOME` para apontar para o diretório de instalação do JDK

### 3. Configuração do Emulador (opcional)

Se você planeja usar um emulador em vez de um dispositivo físico:

1. Abra o Android Studio
2. Vá para Tools > AVD Manager
3. Clique em "Create Virtual Device"
4. Selecione um dispositivo (ex: Pixel 4)
5. Selecione uma imagem do sistema com API 24 ou superior
6. Configure as opções adicionais conforme necessário
7. Clique em "Finish"

## Clonando e Configurando o Projeto

### 1. Clone o Repositório

```bash
git clone https://github.com/brunocarvalhs/FriendsSecrets.git
cd FriendsSecrets
```

### 2. Configuração do Firebase

O projeto utiliza vários serviços do Firebase. Para configurá-los:

1. Acesse o [Console do Firebase](https://console.firebase.google.com/)
2. Crie um novo projeto (ou use um existente)
3. Adicione um aplicativo Android:
   - Use o pacote `br.com.brunocarvalhs.friendssecrets`
   - Baixe o arquivo `google-services.json`
   - Coloque o arquivo na pasta `app/` do projeto

4. Ative os seguintes serviços no console do Firebase:
   - Authentication (habilite o provedor de telefone)
   - Firestore Database
   - Storage
   - Crashlytics
   - Analytics
   - Remote Config

5. Configure as regras de segurança do Firestore:
   ```
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /{document=**} {
         allow read, write: if request.auth != null;
       }
     }
   }
   ```

### 3. Configuração da API do Google Generative AI

1. Acesse o [Console de APIs do Google](https://console.cloud.google.com/apis/dashboard)
2. Crie um novo projeto ou selecione o mesmo projeto do Firebase
3. Ative a API Generative AI
4. Crie uma chave de API

### 4. Configuração das Variáveis de Ambiente

Crie um arquivo `local.properties` na raiz do projeto (se não existir) e adicione:

```properties
# API Key para Google Generative AI
API_KEY=sua_chave_api_aqui

# Para builds de release (opcional)
KEYSTORE_PASSWORD=sua_senha_keystore
KEYSTORE_ALIAS=seu_alias_keystore
KEY_PASSWORD=sua_senha_key
```

### 5. Sincronização do Projeto

1. Abra o projeto no Android Studio
2. Aguarde a sincronização automática do Gradle
3. Se a sincronização não iniciar automaticamente, clique em "Sync Project with Gradle Files"

## Executando o Aplicativo

### Em um Emulador

1. No Android Studio, selecione o emulador configurado no menu suspenso de dispositivos
2. Clique no botão "Run" (ícone de play verde) ou pressione Shift+F10
3. Aguarde o aplicativo ser compilado e instalado no emulador

### Em um Dispositivo Físico

1. Ative o "Modo de desenvolvedor" no seu dispositivo Android:
   - Vá para Configurações > Sobre o telefone
   - Toque 7 vezes em "Número da versão"
   - Volte para Configurações > Opções do desenvolvedor
   - Ative "Depuração USB"

2. Conecte o dispositivo ao computador via USB
3. No Android Studio, selecione seu dispositivo no menu suspenso de dispositivos
4. Clique no botão "Run" (ícone de play verde) ou pressione Shift+F10
5. Aguarde o aplicativo ser compilado e instalado no dispositivo

## Solução de Problemas Comuns

### Erro de Sincronização do Gradle

Se você encontrar erros durante a sincronização do Gradle:

1. Verifique sua conexão com a internet
2. Vá para File > Settings > Build, Execution, Deployment > Gradle
3. Certifique-se de que o "Gradle JDK" está configurado corretamente
4. Tente "File > Invalidate Caches / Restart"

### Erro de Compilação

Se o projeto não compilar:

1. Verifique se todas as dependências estão disponíveis
2. Certifique-se de que o arquivo `google-services.json` está no lugar correto
3. Verifique se as variáveis de ambiente estão configuradas corretamente
4. Execute "Build > Clean Project" e tente novamente

### Erro de Execução

Se o aplicativo não executar ou travar:

1. Verifique os logs no Android Studio (janela Logcat)
2. Certifique-se de que o Firebase está configurado corretamente
3. Verifique se o dispositivo/emulador atende aos requisitos mínimos

## Configuração para Desenvolvimento

### Configuração do Git

Configure seu nome de usuário e email para commits:

```bash
git config user.name "Seu Nome"
git config user.email "seu.email@exemplo.com"
```

### Fluxo de Trabalho com Branches

O projeto segue o modelo de branches:

- `master`: Código de produção
- `develop`: Branch de desenvolvimento principal
- `feature/*`: Branches para novas funcionalidades
- `bugfix/*`: Branches para correções de bugs

Para criar uma nova branch de feature:

```bash
git checkout develop
git pull origin develop
git checkout -b feature/nome-da-feature
```

## Recursos Adicionais

- [Documentação do Android](https://developer.android.com/docs)
- [Documentação do Kotlin](https://kotlinlang.org/docs/home.html)
- [Documentação do Jetpack Compose](https://developer.android.com/jetpack/compose/documentation)
- [Documentação do Firebase](https://firebase.google.com/docs)
- [Documentação da API Generative AI](https://ai.google.dev/docs)

## Suporte

Se você encontrar problemas durante a configuração, entre em contato:

- Abra uma issue no [GitHub](https://github.com/brunocarvalhs/FriendsSecrets/issues)
- Envie um email para [brunocarvalhs@gmail.com](mailto:brunocarvalhs@gmail.com)