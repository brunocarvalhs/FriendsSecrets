import { danger, message, warn, fail } from 'danger'

// Verifica se a descrição do PR está presente e se tem um mínimo de caracteres
if (danger.github.pr.body.length < 10) {
  fail("A descrição do PR deve ter pelo menos 10 caracteres.")
}

// Obtém os arquivos modificados no PR
const modifiedFiles = danger.git.modified_files

// Filtra arquivos Kotlin (.kt) e arquivos de recurso (.xml)
const kotlinFiles = modifiedFiles.filter(file => file.endsWith('.kt'))
const xmlFiles = modifiedFiles.filter(file => file.endsWith('.xml'))

// Mensagens sobre arquivos Kotlin modificados
if (kotlinFiles.length > 0) {
  message(`Foram modificados os arquivos Kotlin: ${kotlinFiles.join(', ')}`)
}

// Mensagens sobre arquivos XML modificados
if (xmlFiles.length > 0) {
  message(`Foram modificados os arquivos XML: ${xmlFiles.join(', ')}`)
}

// Verifica se o PR contém testes (considerando os arquivos de teste Kotlin, .kt, ou arquivos de teste específicos)
const hasTests = modifiedFiles.some(file =>
  file.endsWith('.kt') && (file.includes('Test') || file.includes('spec'))
)

// Se não houver testes, avisa
if (!hasTests) {
  warn("Este PR não contém testes. Considere adicionar testes.")
}

// Sugestão adicional: Verifica se foram adicionados arquivos de Compose
const composeFiles = modifiedFiles.filter(file => file.includes('Composable') || file.includes('Compose'))

if (composeFiles.length > 0) {
  message(`Foram modificados os arquivos de Jetpack Compose: ${composeFiles.join(', ')}`)
}
