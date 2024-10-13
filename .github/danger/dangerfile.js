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

// Verifica se o PR contém novos, modificados ou excluídos testes unitários
function checkForUnitTests(createdFiles, modifiedFiles, deletedFiles) {
  // Padrão que identifica arquivos de testes unitários
  const unitTestPattern = /(\/test\/|Test|Tests|UnitTest|Unit)$/;

  // Filtra os arquivos criados, modificados e excluídos que correspondem ao padrão de teste unitário
  const createdUnitTestFiles = createdFiles.filter(file => {
    const normalizedFile = file.replace(/\\/g, '/'); // Normaliza os separadores de caminho
    return normalizedFile.endsWith('.kt') && unitTestPattern.test(normalizedFile);
  });

  const modifiedUnitTestFiles = modifiedFiles.filter(file => {
    const normalizedFile = file.replace(/\\/g, '/'); // Normaliza os separadores de caminho
    return normalizedFile.endsWith('.kt') && unitTestPattern.test(normalizedFile);
  });

  const deletedUnitTestFiles = deletedFiles.filter(file => {
    const normalizedFile = file.replace(/\\/g, '/'); // Normaliza os separadores de caminho
    return normalizedFile.endsWith('.kt') && unitTestPattern.test(normalizedFile);
  });

  // Verifica se houve criação de novos testes
  if (createdUnitTestFiles.length > 0) {
    message(`Os seguintes novos testes unitários foram criados: ${createdUnitTestFiles.join(', ')}`);
  } else {
    warn("Nenhum novo teste unitário foi criado.");
  }

  // Verifica se houve modificação de testes existentes
  if (modifiedUnitTestFiles.length > 0) {
    message(`Os seguintes testes unitários foram modificados: ${modifiedUnitTestFiles.join(', ')}`);
  } else {
    warn("Nenhum teste unitário existente foi modificado.");
  }

  // Verifica se houve exclusão de testes
  if (deletedUnitTestFiles.length > 0) {
    message(`Os seguintes testes unitários foram excluídos: ${deletedUnitTestFiles.join(', ')}`);
  } else {
    warn("Nenhum teste unitário foi excluído.");
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

  // Obter todos os arquivos do PR: modificados, adicionados e removidos
  const createdFiles = danger.git.created_files;
  const modifiedFiles = danger.git.modified_files;
  const deletedFiles = danger.git.deleted_files;

  const allFiles = [...createdFiles, ...modifiedFiles, ...deletedFiles];

  checkLibsVersionsFile(allFiles);
  checkModifiedFiles(allFiles);
  checkForUnitTests(createdFiles, modifiedFiles, deletedFiles);
  checkForComposeFiles(allFiles);
  checkAndroidCoreFiles(allFiles); // Verifica arquivos principais do projeto Android

  await checkForBlockedLibs(allFiles);  // Verifica bibliotecas bloqueadas
  await checkForDeprecatedLibs(allFiles);  // Verifica bibliotecas depreciadas
}

// Executa as verificações
runPRChecks();