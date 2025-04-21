# Matriz de Rastreabilidade

Este documento estabelece a relação entre os requisitos funcionais e os casos de uso do aplicativo Friends Secrets, permitindo rastrear como cada requisito é implementado e testado.

## Matriz de Requisitos Funcionais x Casos de Uso

| Requisito | Descrição Resumida | Casos de Uso Relacionados |
|-----------|-------------------|--------------------------|
| RF01 | Criação de novos grupos | UC03 |
| RF02 | Exibição e gerenciamento de grupos | UC04 |
| RF03 | Personalização de tema | UC08 |
| RF04 | Autenticação por telefone | UC01 |
| RF05 | Navegação e interface | UC01, UC02, UC03, UC04, UC06, UC07, UC08, UC10 |
| RF06 | Autenticação biométrica | UC01 |
| RF07 | Perfil de usuário | UC02 |
| RF08 | Configurações do aplicativo | UC08, UC10 |
| RF09 | Sorteio de amigos secretos | UC05 |
| RF10 | Chat com IA para sugestões | UC07 |
| RF11 | Gerenciamento de preferências | UC02 |
| RF12 | Compartilhamento de grupos | UC09 |
| RF13 | Feedback e suporte | UC10 |
| RF14 | Revelação de amigo secreto | UC06 |
| RF15 | Onboarding para novos usuários | - |

## Matriz de Casos de Uso x Requisitos Funcionais

| Caso de Uso | Descrição Resumida | Requisitos Relacionados |
|-------------|-------------------|------------------------|
| UC01 | Autenticação de Usuário | RF04, RF05, RF06 |
| UC02 | Gerenciamento de Perfil | RF05, RF07, RF11 |
| UC03 | Criação de Grupo | RF01, RF05 |
| UC04 | Gerenciamento de Grupo | RF02, RF05 |
| UC05 | Sorteio de Amigos Secretos | RF09 |
| UC06 | Visualização de Amigo Secreto | RF05, RF14 |
| UC07 | Obtenção de Sugestões de Presentes | RF05, RF10 |
| UC08 | Personalização de Tema | RF03, RF05, RF08 |
| UC09 | Compartilhamento de Grupo | RF12 |
| UC10 | Envio de Feedback | RF05, RF08, RF13 |

## Matriz de Requisitos Não Funcionais x Implementação

| Requisito | Descrição Resumida | Implementação |
|-----------|-------------------|--------------|
| RNF01 | Desempenho | Firebase Performance, otimização de consultas, cache local |
| RNF02 | Segurança de Dados | Autenticação Firebase, regras de segurança Firestore, biometria |
| RNF03 | Compatibilidade e Responsividade | Jetpack Compose, Material Design 3, layouts responsivos |
| RNF04 | Escalabilidade | Arquitetura Firebase, consultas otimizadas |
| RNF05 | Usabilidade | Material Design, feedback visual, mensagens de erro claras |
| RNF06 | Disponibilidade | Firebase Realtime Database, cache offline |
| RNF07 | Manutenibilidade | Clean Architecture, MVVM, injeção de dependência |
| RNF08 | Monitoramento e Análise | Firebase Crashlytics, Firebase Analytics |
| RNF09 | Privacidade | Política de privacidade, controle de dados do usuário |
| RNF10 | Configuração Remota | Firebase Remote Config |

## Cobertura de Requisitos

### Requisitos Funcionais

- **Totalmente Cobertos por Casos de Uso:** 14/15 (93,3%)
- **Parcialmente Cobertos:** 0/15 (0%)
- **Não Cobertos:** 1/15 (6,7%)

### Requisitos Não Funcionais

- **Com Implementação Identificada:** 10/10 (100%)
- **Sem Implementação Identificada:** 0/10 (0%)

## Observações

1. O requisito RF15 (Onboarding para novos usuários) não está diretamente associado a um caso de uso específico, pois é uma funcionalidade que ocorre automaticamente na primeira execução do aplicativo.

2. Todos os requisitos não funcionais possuem implementações identificadas no código atual do aplicativo.

3. A matriz de rastreabilidade deve ser atualizada sempre que novos requisitos forem adicionados ou modificados, ou quando novos casos de uso forem definidos.