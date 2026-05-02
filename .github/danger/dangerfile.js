const { danger, message, warn, fail } = require('danger');
const fs = require('fs');

// --- Helper Functions ---

function getLibsFromFile(fileName) {
  try {
    const fileContent = fs.readFileSync(fileName, 'utf-8');
    return fileContent.split('\n').map(lib => lib.trim()).filter(lib => lib.length > 0);
  } catch (error) {
    return [];
  }
}

const blockedLibs = getLibsFromFile('.github/danger/blockedLibs.txt');
const deprecatedLibs = getLibsFromFile('.github/danger/deprecatedLibs.txt');

function isAndroidFile(file) {
  const ignoredPrefixes = ['.github/', 'docs/', 'scripts/', '.gradle/', 'fastlane/'];
  return !ignoredPrefixes.some(prefix => file.startsWith(prefix));
}

function hasAndroidChanges(files) {
  const criticalPatterns = ['AndroidManifest.xml', 'build.gradle', 'settings.gradle', 'proguard-rules.pro', '/src/', '/res/'];
  return files.some(file => criticalPatterns.some(pattern => file.includes(pattern)));
}

// --- PR Checks ---

function checkPRDescription() {
  const prDescription = danger.github.pr.body || '';
  if (prDescription.length < 10) {
    fail("### 📝 Descrição insuficiente\nPor favor, forneça uma descrição detalhada das mudanças para facilitar o review.");
  }
}

function checkPRTitle() {
  const prTitle = danger.github.pr.title;
  const pattern = /^(feat|fix|docs|style|refactor|perf|test|chore|build|ci|revert|release|BREAKING CHANGE): .+/;
  if (!pattern.test(prTitle)) {
    fail("### 🔤 Título fora do padrão\nO título do PR deve seguir o [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/).\nExemplo: `feat: add user login` ou `release: v3.0.0`.");
  }
}

function getModule(file) {
  const parts = file.split('/');
  if (parts.length > 1) {
    if (parts[0] === 'core' || parts[0] === 'features') {
      return `:${parts[0]}:${parts[1]}`;
    }
    return `:${parts[0]}`;
  }
  return 'root';
}

function checkModifiedFiles(files) {
  const kotlinFiles = files.filter(f => f.endsWith('.kt'));
  const xmlFiles = files.filter(f => f.endsWith('.xml'));

  if (kotlinFiles.length > 0 || xmlFiles.length > 0) {
    let output = "### 📂 Arquivos Modificados\n\n";

    const modules = {};
    [...kotlinFiles, ...xmlFiles].forEach(file => {
      const module = getModule(file);
      if (!modules[module]) modules[module] = [];
      modules[module].push(file);
    });

    Object.keys(modules).sort().forEach(module => {
      const moduleFiles = modules[module];
      output += `<details><summary><b>📦 ${module} (${moduleFiles.length} arquivos)</b></summary>\n\n- ${moduleFiles.join('\n- ')}\n</details>\n\n`;
    });

    message(output);
  }
}

function checkForUnitTests(created, modified) {
  const testPattern = /Test/;
  const hasTests = [...created, ...modified].some(f => testPattern.test(f));
  const isFeatureOrFix = danger.github.pr.title.startsWith('feat') || danger.github.pr.title.startsWith('fix');

  if (!hasTests && isFeatureOrFix) {
    warn("⚠️ **Aviso:** Nenhum arquivo de teste foi identificado neste PR. Para `feat` e `fix`, recomendamos fortemente a inclusão de testes unitários.");
  } else if (hasTests) {
    message("✅ **Garantia de Qualidade:** Testes identificados no PR. Bom trabalho!");
  }
}

function checkForComposeFiles(files) {
  const composeFiles = files.filter(f => f.includes('Composable') || f.includes('Compose'));
  if (composeFiles.length > 0) {
    message(`### ⚛️ UI Compose\nDetectamos alterações em **${composeFiles.length}** arquivos do Jetpack Compose.`);
  }
}

function checkAndroidCoreFiles(files) {
  const criticalPatterns = ['AndroidManifest.xml', 'build.gradle', 'settings.gradle', 'proguard-rules.pro', 'libs.versions.toml'];
  const changedCore = files.filter(f => criticalPatterns.some(p => f.includes(p)));

  if (changedCore.length > 0) {
    warn(`### 🛠️ Core do Projeto\nForam alterados arquivos sensíveis:\n<details><summary>Ver arquivos críticos</summary>\n\n- ${changedCore.join('\n- ')}\n</details>`);
  }
}

async function checkDependencies(files) {
  const gradleFiles = files.filter(f => f.endsWith('.kts') || f.includes('toml'));
  if (gradleFiles.length === 0) return;

  const foundBlocked = [];
  const foundDeprecated = [];

  for (const file of gradleFiles) {
    const content = await danger.git.diffForFile(file);
    if (!content) continue;
    const addedLines = content.diff.split('\n').filter(l => l.startsWith('+'));

    blockedLibs.forEach(lib => {
      if (addedLines.some(line => line.includes(lib))) foundBlocked.push({ lib, file });
    });

    deprecatedLibs.forEach(lib => {
      if (addedLines.some(line => line.includes(lib))) foundDeprecated.push({ lib, file });
    });
  }

  if (foundBlocked.length > 0) {
    fail(`### 🚫 Dependências Bloqueadas\nRemova as seguintes libs proibidas:\n${foundBlocked.map(f => `- \`${f.lib}\` (em ${f.file})`).join('\n')}`);
  }

  if (foundDeprecated.length > 0) {
    warn(`### ⚠️ Dependências Depreciadas\nConsidere atualizar as seguintes bibliotecas:\n${foundDeprecated.map(f => `- \`${f.lib}\` (em ${f.file})`).join('\n')}`);
  }
}

// --- Main execution ---

async function runPRChecks() {
  checkPRDescription();
  checkPRTitle();

  const allFiles = [...danger.git.created_files, ...danger.git.modified_files];
  const androidFiles = allFiles.filter(isAndroidFile);

  if (androidFiles.length > 0) {
    checkModifiedFiles(androidFiles);
    await checkDependencies(androidFiles);

    const criticalPatterns = ['/src/', '/res/', 'AndroidManifest.xml', 'build.gradle'];
    const hasCoreChanges = androidFiles.some(f => criticalPatterns.some(p => f.includes(p)));

    if (hasCoreChanges) {
      checkForUnitTests(danger.git.created_files, danger.git.modified_files);
      checkForComposeFiles(androidFiles);
      checkAndroidCoreFiles(androidFiles);
    }
  }
}

runPRChecks();
