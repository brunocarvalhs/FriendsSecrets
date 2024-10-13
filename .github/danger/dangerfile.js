const { danger, message, warn, fail } = require('danger');
const fs = require('fs');

// Função para ler bibliotecas bloqueadas e depreciadas dos arquivos de texto
function getLibsFromFile(fileName) {
  try {
    const fileContent = fs.readFileSync(fileName, 'utf-8');
    return fileContent.split('\n').map(lib => lib.trim()).filter(lib => lib.length > 0);
  } catch (error) {
    if (error.code === 'ENOENT') {
      message(`Arquivo ${fileName} não encontrado. Certifique-se de que o arquivo existe e está no diretório correto.`);
    } else {
      fail(`Erro ao ler o arquivo ${fileName}: ${error.message}`);
    }
    return [];
  }
}

// Carrega bibliotecas bloqueadas e depreciadas dos arquivos .txt
const blockedLibs = getLibsFromFile('.github/danger/blockedLibs.txt');
const deprecatedLibs = getLibsFromFile('.github/danger/deprecatedLibs.txt');

// Verifica se a descrição do PR está presente e tem o mínimo de caracteres
function checkPRDescription() {
  const prDescription = danger.github.pr.body || '';
  if (prDescription.length < 10) {
    fail("A descrição do PR deve ter pelo menos 10 caracteres.");
  }
}

// Verifica se o título do PR segue as regras do Conventional Commits
function checkPRTitle() {
  const prTitle = danger.github.pr.title;
  const conventionalCommitPattern = /^(feat|fix|docs|style|refactor|perf|test|chore|build|ci|revert|BREAKING CHANGE): .+/;
  if (!conventionalCommitPattern.test(prTitle)) {
    fail("O título do PR deve seguir a convenção de commits. Exemplo: `feat: nova funcionalidade`.");
  }
}

// Verifica se o arquivo libs.versions.gradle foi alterado
function checkLibsVersionsFile(modifiedFiles) {
  if (modifiedFiles.includes('libs.versions.gradle')) {
    message("O arquivo `libs.versions.gradle` foi alterado.");
  }
}

// Filtra e verifica arquivos Kotlin e XML
function checkModifiedFiles(modifiedFiles) {
  const kotlinFiles = modifiedFiles.filter(file => file.endsWith('.kt'));
  const xmlFiles = modifiedFiles.filter(file => file.endsWith('.xml'));

  if (kotlinFiles.length > 0) {
    message(`Foram modificados os arquivos Kotlin: ${kotlinFiles.join(', ')}`);
  }

  if (xmlFiles.length > 0) {
    message(`Foram modificados os arquivos XML: ${xmlFiles.join(', ')}`);
  }
}

// Verifica se o PR contém novos ou modificados testes unitários
function checkForUnitTests(modifiedFiles) {
  // Padrão que identifica arquivos de testes unitários
  const unitTestPattern = /(\/test\/|Test|Tests|UnitTest|Unit)$/;

  // Filtra apenas os arquivos Kotlin que são testes unitários
  const unitTestFiles = modifiedFiles.filter(file => {
    const normalizedFile = file.replace(/\\/g, '/'); // Normaliza os separadores de caminho
    return normalizedFile.endsWith('.kt') && unitTestPattern.test(normalizedFile);
  });

  if (unitTestFiles.length > 0) {
    message(`Os seguintes testes unitários foram modificados ou adicionados: ${unitTestFiles.join(', ')}`);
  } else {
    warn("Nenhum novo teste unitário foi adicionado ou modificado.");
  }
}

// Verifica se há arquivos relacionados ao Jetpack Compose
function checkForComposeFiles(modifiedFiles) {
  const composeFiles = modifiedFiles.filter(file => file.includes('Composable') || file.includes('Compose'));

  if (composeFiles.length > 0) {
    message(`Foram modificados os arquivos de Jetpack Compose: ${composeFiles.join(', ')}`);
  }
}

// Verifica se arquivos críticos do projeto Android foram modificados
function checkAndroidCoreFiles(modifiedFiles) {
  const criticalFilesPatterns = [
    'AndroidManifest.xml',        // O manifesto principal
    'build.gradle',               // Arquivos de configuração Gradle
    'settings.gradle',            // Arquivo de configuração do projeto
    'proguard-rules.pro',         // Regras de obfuscação e otimização
    '/src/',                      // Diretório de código-fonte de qualquer módulo
    '/res/',                      // Diretório de recursos (layouts, etc.)
    '/assets/',                   // Diretório de assets
    '/libs/',                     // Diretório de bibliotecas locais
    '.gradle',                    // Qualquer arquivo Gradle
  ];

  // Verifica se algum dos arquivos modificados corresponde aos padrões críticos
  const criticalFilesChanged = modifiedFiles.some(file =>
    criticalFilesPatterns.some(pattern => file.includes(pattern))
  );

  if (criticalFilesChanged) {
    message("Arquivos principais do projeto Android foram modificados.");
  } else {
    warn("Nenhum arquivo principal do projeto Android foi modificado.");
  }
}

// Verifica se bibliotecas bloqueadas foram adicionadas ou modificadas
async function checkForBlockedLibs(modifiedFiles) {
  const gradleFiles = modifiedFiles.filter(file => file.endsWith('build.gradle.kts') || file.includes('libs.versions.toml'));

  for (const file of gradleFiles) {
    const fileContent = await danger.git.diffForFile(file);
    if (fileContent && fileContent.diff) {
      const addedLines = fileContent.diff.split('\n').filter(line => line.startsWith('+'));

      blockedLibs.forEach(blockedLib => {
        const isBlockedLibAdded = addedLines.some(line => line.includes(blockedLib) || line.includes(blockedLib.split(':')[0]));
        if (isBlockedLibAdded) {
          fail(`A biblioteca bloqueada ${blockedLib} foi adicionada ou modificada no arquivo ${file}. Remova ou substitua esta dependência.`);
        }
      });
    }
  }
}

// Verifica se bibliotecas depreciadas foram adicionadas ou modificadas
async function checkForDeprecatedLibs(modifiedFiles) {
  const gradleFiles = modifiedFiles.filter(file => file.endsWith('build.gradle.kts') || file.includes('libs.versions.toml'));

  for (const file of gradleFiles) {
    const fileContent = await danger.git.diffForFile(file);
    if (fileContent && fileContent.diff) {
      const addedLines = fileContent.diff.split('\n').filter(line => line.startsWith('+'));

      deprecatedLibs.forEach(deprecatedLib => {
        const isDeprecatedLibAdded = addedLines.some(line => line.includes(deprecatedLib) || line.includes(deprecatedLib.split(':')[0]));
        if (isDeprecatedLibAdded) {
          warn(`A biblioteca depreciada ${deprecatedLib} foi adicionada ou modificada no arquivo ${file}. Considere atualizar esta dependência.`);
        }
      });
    }
  }
}

// Função principal para rodar todas as verificações
async function runPRChecks() {
  checkPRDescription();
  checkPRTitle();

  const modifiedFiles = danger.git.modified_files;
  checkLibsVersionsFile(modifiedFiles);
  checkModifiedFiles(modifiedFiles);
  checkForUnitTests(modifiedFiles);
  checkForComposeFiles(modifiedFiles);
  checkAndroidCoreFiles(modifiedFiles); // Verifica arquivos principais do projeto Android

  await checkForBlockedLibs(modifiedFiles);  // Verifica bibliotecas bloqueadas
  await checkForDeprecatedLibs(modifiedFiles);  // Verifica bibliotecas depreciadas
}

// Executa as verificações
runPRChecks();