const { danger, message, warn, fail } = require('danger');

// Verifica se a descrição do PR está presente e se tem um mínimo de caracteres
if (danger.github.pr.body.length < 10) {
  fail("A descrição do PR deve ter pelo menos 10 caracteres.");
}

// Obtém o título do PR
const prTitle = danger.github.pr.title;

// Verifica se o título do PR segue as regras do Conventional Commits
const conventionalCommitPattern = /^(feat|fix|docs|style|refactor|perf|test|chore|build|ci|revert|BREAKING CHANGE): .+/;
if (!conventionalCommitPattern.test(prTitle)) {
  fail("O título do PR deve seguir a convenção de commits. Exemplo: `feat: nova funcionalidade`.");
}

// Obtém os arquivos modificados no PR
const modifiedFiles = danger.git.modified_files;

// Verifica se o arquivo libs.versions.gradle foi alterado
const libsVersionsFileChanged = modifiedFiles.includes('libs.versions.gradle');
if (libsVersionsFileChanged) {
  message("O arquivo `libs.versions.gradle` foi alterado.")
}

// Filtra arquivos Kotlin (.kt) e arquivos de recurso (.xml)
const kotlinFiles = modifiedFiles.filter(file => file.endsWith('.kt'));
const xmlFiles = modifiedFiles.filter(file => file.endsWith('.xml'));

// Mensagens sobre arquivos Kotlin modificados
if (kotlinFiles.length > 0) {
  message(`Foram modificados os arquivos Kotlin: ${kotlinFiles.join(', ')}`);
}

// Mensagens sobre arquivos XML modificados
if (xmlFiles.length > 0) {
  message(`Foram modificados os arquivos XML: ${xmlFiles.join(', ')}`);
}

// Verifica se o PR contém testes (considerando os arquivos de teste Kotlin, .kt, ou arquivos de teste específicos)
const hasTests = modifiedFiles.some(file =>
  file.endsWith('.kt') && (file.includes('Test') || file.includes('spec'))
);

// Se não houver testes, avisa
if (!hasTests) {
  warn("Este PR não contém testes. Considere adicionar testes.");
}

// Sugestão adicional: Verifica se foram adicionados arquivos de Compose
const composeFiles = modifiedFiles.filter(file => file.includes('Composable') || file.includes('Compose'));

if (composeFiles.length > 0) {
  message(`Foram modificados os arquivos de Jetpack Compose: ${composeFiles.join(', ')}`);
}

// Verifica se há alguma quebra de compatibilidade no título do PR
if (prTitle.includes('BREAKING CHANGE')) {
  message("O PR contém uma quebra de compatibilidade (BREAKING CHANGE).");
}

// Verifica se o CHANGELOG foi preenchido
const changelogFilled = danger.github.pr.body.includes('## [CHANGELOG]');

if (!changelogFilled) {
  fail("Por favor, preencha a seção CHANGELOG na descrição do PR. Exemplo:\n\n## [CHANGELOG]\n- Descrição das alterações...");
} else {
  message("A seção CHANGELOG foi preenchida.");
}
