# STATE

## Decisions

### AD-001
- **Decision**: Arquitetura Multi-módulo com Clean Architecture e Hilt.
- **Reason**: Proporciona separação clara de responsabilidades, escalabilidade e facilita testes automatizados.
- **Trade-off**: Aumenta a complexidade inicial de configuração do Gradle e injeção de dependência.
- **Scope**: Todo o projeto Android.
- **Date**: 2026-08-20
- **Status**: active

### AD-002
- **Decision**: Lógica de Domínio (UseCases) escopada por Feature.
- **Reason**: Garante que regras de negócio específicas de uma funcionalidade fiquem próximas à sua implementação, evitando um módulo de domínio central inchado e frágil.
- **Trade-off**: Entidades compartilhadas precisam ser movidas para o `:core:domain`, o que exige disciplina na separação.
- **Scope**: Pacotes `domain` dentro de `:features:*`.
- **Date**: 2026-08-20
- **Status**: active

### AD-003
- **Decision**: Navegação centralizada no módulo `:core:navigation`.
- **Reason**: Permite que features naveguem entre si sem ter dependência direta umas das outras, usando contratos e rotas abstratas.
- **Trade-off**: Requer boilerplate adicional para registrar rotas e inicializadores de feature.
- **Scope**: Todos os módulos de feature.
- **Date**: 2026-08-20
- **Status**: active

### AD-004
- **Decision**: Firebase como Principal Data Source (Data Layer).
- **Reason**: Agilidade no desenvolvimento de funcionalidades de tempo real (Firestore), autenticação e configuração remota sem necessidade de um backend próprio complexo.
- **Trade-off**: Dependência de vendor (Firebase) e possíveis custos de escala.
- **Scope**: Repositórios nos módulos de feature e `:core:network`.
- **Date**: 2026-08-20
- **Status**: active

### AD-005
- **Decision**: Jetpack Compose com Material 3 para UI.
- **Reason**: Padrão moderno do Android para criação de interfaces declarativas e suporte nativo a temas dinâmicos.
- **Trade-off**: Curva de aprendizado para desenvolvedores acostumados com XML.
- **Scope**: Todos os módulos de apresentação.
- **Date**: 2026-08-20
- **Status**: active

## Handoff

- **Feature**: baseline-mapping
- **Phase / Task**: Completed
- **Completed**: AD-001, AD-002, AD-003, AD-004, AD-005, Documentation Sync
- **In-progress**: none
- **Next step**: Definir a primeira funcionalidade para implementação via SDD ou realizar uma análise de cobertura de testes.
- **Blockers**: none
- **Uncommitted files**: none
- **Branch**: main
