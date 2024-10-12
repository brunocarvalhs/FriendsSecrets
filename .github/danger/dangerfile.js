import { danger, message, warn, fail } from 'danger'

// Verifica se a descrição do PR está presente
if (danger.github.pr.body.length < 10) {
  fail("A descrição do PR deve ter pelo menos 10 caracteres.")
}

// Verifica se há arquivos Java ou XML modificados
const modifiedFiles = danger.git.modified_files

const javaFiles = modifiedFiles.filter(file => file.endsWith('.java'))
const xmlFiles = modifiedFiles.filter(file => file.endsWith('.xml'))

if (javaFiles.length > 0) {
  message(`Foram modificados os arquivos Java: ${javaFiles.join(', ')}`)
}

if (xmlFiles.length > 0) {
  message(`Foram modificados os arquivos XML: ${xmlFiles.join(', ')}`)
}

// Verifica se o PR contém testes
const hasTests = modifiedFiles.some(file => file.includes('test') || file.includes('spec'))

if (!hasTests) {
  warn("Este PR não contém testes. Considere adicionar testes.")
}
